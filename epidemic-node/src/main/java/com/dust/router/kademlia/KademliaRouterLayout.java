package com.dust.router.kademlia;

import com.dust.NodeConfig;
import com.dust.fundation.EpidemicUtils;
import com.dust.grpc.kademlia.FindRequest;
import com.dust.grpc.kademlia.KademliaServiceGrpc;
import com.dust.grpc.kademlia.NodeInfo;
import com.dust.logs.LogFormat;
import com.dust.logs.LogParser;
import com.dust.logs.Logger;
import com.dust.logs.entity.LayoutLog;
import com.dust.router.RouterLayout;
import io.grpc.ManagedChannelBuilder;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.*;

public class KademliaRouterLayout extends RouterLayout {

    /**
     * 网络路由模块的快照文件名称
     */
    public static final String SNAPSHOT_FILENAME = "node.cache";

    /**
     * 当前路由版本号
     */
    public static final long VERSION = 1L;

    /**
     * 桶管理器
     */
    private KademliaBucket bucket;

    /**
     * 当前节点的节点id
     */
    private String myId;

    public static RouterLayout create(NodeConfig config) throws IOException {
        String tmp = config.getStoragePath();
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
    }

    @Override
    public boolean isCompatibleVersion(long version) {
        return this.version == version;
    }

    @Override
    public void haveNewNode(String nodeId, String host, int port) {
        bucket.ping(nodeId, host, port);
    }

    /**
     * 从快照文件中加载路由表信息,如果有的话
     * @throws IOException
     *  加载文件失败
     */
    @Override
    public void before() throws IOException {
        File f = new File(path, SNAPSHOT_FILENAME);
        if (!f.exists()) {
            //不存在快照文件
            bucket = new KademliaBucket(config, myId);
            //尝试读取日志
            readLog();
            findMe();
            return;
        }

        var snapshot = new RandomAccessFile(f, "rw");
        snapshot.seek(0);
        //尝试获取文件的读写锁
        final FileChannel fileChannel = snapshot.getChannel();

        if (!EpidemicUtils.checkHead(head, snapshot)) {
            //如果头文件读取失败则表示数据异常，不读取数据
            //等到后面进行持久化的时候将原文件删除
            snapshot.close();
            return;
        }

        this.myId = EpidemicUtils.readToSHA1(snapshot);
        this.bucket = new KademliaBucket(config, myId);
        while (snapshot.getFilePointer() < snapshot.length()) {
            var node = NodeTriadRouterNode.fromFile(snapshot);
            if (Objects.isNull(node)) {
                continue;
            }
            bucket.add(node);
        }
        snapshot.close();
        readLog();
        findMe();
    }

    /**
     * 如果当前节点是第一次建立则通过联系人节点从集群中获取其他节点信息
     */
    public void findMe() {
        if (!bucket.isEmpty()) {
            //桶中有数据，不再寻找朋友
            return;
        }

        var info = NodeInfo.newBuilder()
                .setPort(config.getNodePort())
                .setNodeId(myId)
                .build();
        var queue = new LinkedList<NodeTriadRouterNode>();

        queue.add(new NodeTriadRouterNode("-", config.getContactHost(), config.getContactPort()));
        while (!queue.isEmpty() && !bucket.isEmpty()) {
            var node = queue.poll();
            if (Objects.isNull(node)) {
                continue;
            }

            var channel = ManagedChannelBuilder
                    .forAddress(node.getHost(), node.getPort())
                    .usePlaintext()
                    .build();
            var client = KademliaServiceGrpc.newBlockingStub(channel);
            var req = FindRequest.newBuilder()
                    .setTargetId(myId)
                    .setNodeInfo(info)
                    .build();
            var res = client.findNode(req);
            while (res.hasNext()) {
                var re = res.next();
                if (re.getCode() != 1 || myId.equals(re.getNodeId())
                    || bucket.contains(re.getNodeId())) {
                    continue;
                }
                var newNode = new NodeTriadRouterNode(re.getNodeId(),
                        re.getHost(), re.getPort());
                bucket.add(newNode);
                queue.add(newNode);
            }
        }
    }

    /**
     * 读取日志
     */
    private void readLog() {
        File file = new File("log/layout.log");
        if (!file.exists()) {
            return;
        }

        var findEd = new HashSet<String>();
        var list = new ArrayList<LayoutLog>();
        try (var raf = new RandomAccessFile(file, "r")) {
            if (raf.length() == 0) {
                return;
            }
            long start = raf.getFilePointer();
            long nextEnd = start + raf.length() - 1;
            String result;
            raf.seek(nextEnd);
            int c = -1;
            while (nextEnd >= start) {
                c = raf.read();
                if (c == '\r' || c == '\n') {
                    result = raf.readLine();
                    LayoutLog layoutLog = LogParser.parser.parseLayout(result);
                    if (Objects.nonNull(layoutLog)) {
                        if (bucket.contains(layoutLog.getNodeId())
                                || findEd.contains(layoutLog.getNodeId())) {
                            break;
                        }

                        findEd.add(layoutLog.getNodeId());
                        list.add(layoutLog);
                    }
                    nextEnd -= 1;
                }
                nextEnd -= 1;
                if (nextEnd >= 0) {
                    raf.seek(nextEnd);
                    if (nextEnd == 0) {
                        LayoutLog layoutLog = LogParser.parser.parseLayout(raf.readLine());
                        if (Objects.nonNull(layoutLog)) {
                            if (bucket.contains(layoutLog.getNodeId())
                                    || findEd.contains(layoutLog.getNodeId())) {
                                break;
                            }

                            findEd.add(layoutLog.getNodeId());
                            list.add(layoutLog);
                        }
                    }
                }
            }
        } catch (IOException e) {
            Logger.systemLog.error(LogFormat.SYSTEM_ERROR_FORMAT, "读取路由节点操作日志文件失败", e.getMessage());
        }

        list.forEach(node -> bucket.add(node.toNode()));
    }

    @Override
    public String getMyId() {
        return myId;
    }

    @Override
    public List<NodeTriad> findNode(String key) {
        return bucket.findNode(key);
    }

}
