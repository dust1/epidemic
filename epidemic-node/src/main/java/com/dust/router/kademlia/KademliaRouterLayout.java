package com.dust.router.kademlia;

import com.dust.NodeConfig;
import com.dust.fundation.EpidemicUtils;
import com.dust.grpc.ClientProxy;
import com.dust.grpc.kademlia.FindRequest;
import com.dust.grpc.kademlia.KademliaServiceGrpc;
import com.dust.grpc.kademlia.NodeInfo;
import com.dust.logs.LogFormat;
import com.dust.logs.LogParser;
import com.dust.logs.LogReader;
import com.dust.logs.Logger;
import com.dust.logs.entity.LayoutLog;
import com.dust.router.RouterLayout;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class KademliaRouterLayout extends RouterLayout {

    /**
     * 网络路由模块的快照文件名称
     */
    public static final String SNAPSHOT_FILENAME = "node.cache";

    /**
     * 日志文件
     */
    private static final String LOG_FILE = "log/layout.log";

    /**
     * 桶管理器
     */
    private KademliaBucket bucket;

    /**
     * 当前节点的节点id
     */
    private String myId;

    /**
     * 节点是否在线
     */
    private boolean online;

    public static RouterLayout create(NodeConfig config) throws IOException {
        String tmp = config.getRouterPath();
        if (!tmp.endsWith("/")) {
            tmp += "/";
        }
        byte[] head = {0xC, 0xA, 0xF, 0xE};
        return new KademliaRouterLayout(tmp, config, 1L,
                ".router_version", head);
    }

    public KademliaRouterLayout(String path, NodeConfig config,
                                long version, String versinFileName,
                                byte[] head) throws IOException {
        super(path, config, version, versinFileName, head);
        this.myId = EpidemicUtils.randomNodeId(config.getNodeSalt());
        this.online = false;
    }

    /**
     * 从快照文件中加载路由表信息,如果有的话
     * @throws IOException
     *  加载文件失败
     */
    @Override
    public void before() throws IOException {
        //从版本配置文件中加载id
        readId();
        //加载id完成后创建id对应的路由节点
        this.bucket = new KademliaBucket(config, myId);
        //从本地快照文件中加载持久化节点
        loadLocalNode();
        //持久化文件加载完后从日志记录中读取未保存的节点
        readLog();
        //如果上述操作都没有获取到节点信息，则从关联节点往集群请求
        try {
            loadCluster();
        } catch (StatusRuntimeException e) {
            Logger.systemLog.warn(LogFormat.SYSTEM_INFO_FORMAT, "节点与关联节点通信失败，当前为单机模式");
            return;
        }
        if (bucket.size() > 0) {
            online = true;
        }
    }


    @Override
    public boolean isCompatibleVersion(long version) {
        return this.version == version;
    }

    @Override
    public void haveNewNode(String nodeId, String host, int port) {
        bucket.checkNewNode(nodeId, host, port);
        if (bucket.canSave()) {
            saveLocal();
        }
    }

    @Override
    public String getMyId() {
        return myId;
    }

    @Override
    public List<NodeTriad> findNode(String key) {
        return bucket.findNode(key);
    }

    /*
    加载本地快照文件中的节点信息
     */
    private void loadLocalNode() throws IOException {
        var file = new File(path, SNAPSHOT_FILENAME);
        if (!file.exists()) {
            return;
        }
        try (var raf = new RandomAccessFile(file, "r")) {
            raf.seek(0);
            if (!EpidemicUtils.checkHead(head, raf)) {
                //如果头文件读取失败则表示数据异常，不读取数据
                //等到后面进行持久化的时候将原文件删除
                return;
            }
            while (raf.getFilePointer() < raf.length()) {
                var node = NodeTriadRouterNode.fromFile(raf);
                if (Objects.isNull(node))
                    continue;
                bucket.add(node);
            }
        }
    }

    /*
    加载集群节点
    如果当前节点是第一次建立则通过联系人节点从集群中获取其他节点信息
     */
    private void loadCluster() {
        if (!bucket.isEmpty()) {
            return;
        }
        var client = ClientProxy.create(getMyId(), config.getNodePort());
        var queue = new LinkedList<NodeTriad>();
        queue.add(new NodeTriad("-", config.getContactHost(),
                config.getContactPort()));
        while (bucket.size() < config.getBucketKey() && !queue.isEmpty()) {
            var node = queue.poll();
            var findResult = client.findNode(node.getHost(), node.getPort(), getMyId());
            for (var result : findResult) {
                var routerNode = new NodeTriadRouterNode(result.getNodeId(),
                        result.getHost(), result.getPort());
                if (!bucket.contains(result.getNodeId())) {
                    queue.add(routerNode);
                    bucket.add(routerNode);
                }
            }
        }
    }

    /*
     读取本地日志，将日志中的节点重新加入到路由表中
     */
    private void readLog() {
        var logFile = new File(LOG_FILE);
        if (!logFile.exists()) {
            return;
        }
        try (var reader = LogReader.create(LOG_FILE)) {
            while (reader.hasNext()) {
                String logLine = reader.next();
                var layout = LayoutLog.parser(logLine);
                if (Objects.isNull(layout))
                    continue;
                if (bucket.contains(layout.getNodeId()))
                    break;
                bucket.add(layout.toNode());
            }
        } catch (IOException e) {
            Logger.systemLog.error(LogFormat.SYSTEM_ERROR_FORMAT, "读取日志出错", e.getMessage());
        }
    }

    /*
     * 将节点信息持久化到本地
     */
    private void saveLocal() {
        var nodes = bucket.cloneBucket();
        var temp = new File(path, SNAPSHOT_FILENAME + "_temp");
        try (var raf = new RandomAccessFile(temp, "rw");
            var channel = raf.getChannel()) {
            raf.write(head);
            for (var node : nodes) {
                channel.write(node.toBuffer());
            }
        } catch (IOException e) {
            Logger.systemLog.error(LogFormat.SYSTEM_ERROR_FORMAT, "将路由节点持久化到本地异常", e.getMessage());
            return;
        }

        if (!temp.renameTo(new File(path, SNAPSHOT_FILENAME))) {
            Logger.systemLog.error(LogFormat.SYSTEM_ERROR_FORMAT, "将路由节点持久化到本地异常", "文件重命名失败");
        }
    }

    /*
     * 根据给定的文件对象句柄尝试从中获取节点Id，
     * 如果id不存在则返回一个随机生成的ID
     * @param raf 文件句柄
     * @return 如果本地文件不存在ID，则返回一个随机ID，并将这个ID持久化到磁盘中
     */
    private void readId() {
        var versionFile = new File(path, versionFileName);
        if (versionFile.length() < EpidemicUtils.SHA1_LENGTH) {
            saveId();
            return;
        }
        this.myId = readLocalId();
    }

    /*
     * 将id持久化
     */
    private void saveId() {
        var temp = new File(path, versionFileName + "_temp");
        try (var raf = new RandomAccessFile(temp, "rw")) {
            raf.seek(0);
            raf.writeLong(version);
            raf.write(myId.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            Logger.systemLog.error(LogFormat.SYSTEM_ERROR_FORMAT, "将ID持久化本地失败", e.getMessage());
        }

        if (!temp.renameTo(new File(path, versionFileName))) {
            Logger.systemLog.error(LogFormat.SYSTEM_ERROR_FORMAT, "版本文件重命名失败", "未知");
        }
    }

    /*
     * 尝试读取节点保存在本地的ID
     */
    private String readLocalId() {
        var versionFile = new File(path, versionFileName);
        String result = myId;
        try (var raf = new RandomAccessFile(versionFile, "r")) {
            raf.seek(0);
            raf.readLong();
            result = EpidemicUtils.readToSHA1(raf);
        } catch (IOException e) {
            Logger.systemLog.error(LogFormat.SYSTEM_ERROR_FORMAT, "从版本文件中读取本地id失败", e.getMessage());
        }
        return result;
    }

}
