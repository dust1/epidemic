package com.dust.router.kademlia;

import com.dust.NodeConfig;
import com.dust.fundation.EpidemicUtils;
import com.dust.router.RouterLayout;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Objects;

public class KademliaRouterLayout extends RouterLayout {

    /**
     * 路由持久化数据头
     */
    public static final byte[] HEAD = {0xC, 0xA, 0xF, 0xE};

    /**
     * 当前路由版本号
     */
    private static final long VERSION = 1L;

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

        System.out.println(bucket);
        bucket.startTimer();
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
