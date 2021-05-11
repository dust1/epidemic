package com.dust.router;

import com.dust.core.NodeConfig;
import com.dust.fundation.EpidemicUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class KademliaRouterLayout extends RouterLayout {

    /**
     * 路由持久化数据头
     */
    private static final byte[] HEAD = {0xC, 0xA, 0xF, 0xE};

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
            bucket = new KademliaBucket(config.getBucketKey(), myId);
            return;
        }

        if (Objects.isNull(snapshot)) {
            snapshot = new RandomAccessFile(f, "rw");
        }
        snapshot.seek(0);
        //尝试获取文件的读写锁
        final FileChannel fileChannel = snapshot.getChannel();

        if (!EpidemicUtils.checkHead(HEAD, snapshot)) {
            snapshot.seek(0);
            fileChannel.close();
            return;
        }

        this.myId = EpidemicUtils.readToSHA1(snapshot);
        this.bucket = new KademliaBucket(config.getBucketKey(), myId);

        while (snapshot.getFilePointer() < snapshot.length()) {
            var node = NodeTriadRouterNode.fromFile(snapshot);
            if (Objects.isNull(node)) {
                continue;
            }
            bucket.add(node);
        }
        fileChannel.close();
    }

    @Override
    public String getMyId() {
        return myId;
    }

    @Override
    public void ping(String nodeId, String host, int port) {
        bucket.ping(nodeId, host, port);
        System.out.println(bucket.toString());
    }

    @Override
    public List<NodeTriad> findNode(String key) {
        return bucket.findNode(key);
    }

    @Override
    public void addNode(NodeTriad newNode) {

    }

    @Override
    protected boolean isCompatibleVersion(long version) {
        return true;
    }

    @Override
    protected long getVersion() {
        return 1L;
    }

    @Override
    protected int getPersistenceNodeSize() {
        return -1;
    }

}
