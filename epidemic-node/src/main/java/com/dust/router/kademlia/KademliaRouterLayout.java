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
     * 路由持久化数据头
     */
    public static final byte[] HEAD = {0xC, 0xA, 0xF, 0xE};

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

    public KademliaRouterLayout(NodeConfig config) throws IOException {
        super(config);
        this.myId = EpidemicUtils.randomNodeId(config.getNodeSalt());
    }

    /**
     * 从快照文件中加载路由表信息,如果有的话
     * @throws IOException
     *  加载文件失败
     */
    @Override
    public void load() throws IOException {
        File f = new File(routerPath, SNAPSHOT_FILENAME);
        if (!f.exists()) {
            //不存在快照文件
            bucket = new KademliaBucket(config, myId);
            //初始化桶的定时任务
            bucket.initTimer(config);
            readLog();
            bucket.startTimer();
            return;
        }

        var snapshot = new RandomAccessFile(f, "rw");
        snapshot.seek(0);
        //尝试获取文件的读写锁
        final FileChannel fileChannel = snapshot.getChannel();

        if (!EpidemicUtils.checkHead(HEAD, snapshot)) {
            //如果头文件读取失败则表示数据异常，不读取数据
            //等到后面进行持久化的时候将原文件删除
            snapshot.close();
            return;
        }

        this.myId = EpidemicUtils.readToSHA1(snapshot);
        this.bucket = new KademliaBucket(config, myId);
        //初始化桶的定时任务
        bucket.initTimer(config);
        while (snapshot.getFilePointer() < snapshot.length()) {
            var node = NodeTriadRouterNode.fromFile(snapshot);
            if (Objects.isNull(node)) {
                continue;
            }
            bucket.add(node);
        }
        snapshot.close();
        readLog();
        bucket.startTimer();
    }

    @Override
    public boolean findFriend() {
        if (!bucket.isEmpty() || config.getContactPort() == -1) {
            //桶中有数据，不再寻找朋友
            return true;
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
        return bucket.isEmpty();
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
    public void ping(String nodeId, String host, int port) {
        bucket.ping(nodeId, host, port);
    }

    @Override
    public List<NodeTriad> findNode(String key) {
        return bucket.findNode(key);
    }

    @Override
    protected boolean isCompatibleVersion(long version) {
        return VERSION == version;
    }

    @Override
    protected long getVersion() {
        return VERSION;
    }

}
