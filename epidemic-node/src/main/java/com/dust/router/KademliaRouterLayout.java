package com.dust.router;

import com.dust.core.NodeConfig;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Objects;

public class KademliaRouterLayout extends RouterLayout {

    /**
     * 桶管理器
     */
    private KademliaBucket bucket;

    private int k;

    public KademliaRouterLayout(NodeConfig config) throws IOException {
        super(config);
        this.k = config.getBucketKey();
        this.bucket = new KademliaBucket(config.getBucketKey());
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
            return;
        }
        bucket.clear();

        snapshot = new RandomAccessFile(f, "rw");
        snapshot.seek(0);
        //尝试获取文件的读写锁
        FileChannel fileChannel = snapshot.getChannel();
        final long fileLen = f.length();
        final int objLen = getPersistenceNodeSize();
        final long objNum = (long) Math.ceil(fileLen / objLen);
        for (long i = 0; i < objNum; i++) {
            var node = NodeTriadRouterNode.fromFile(fileChannel);
            if (Objects.nonNull(node)) {
                bucket.add(node);
            }
        }
    }

    @Override
    public List<NodeTriad> findNode(String key) {
        return null;
    }

    @Override
    public void addNode(NodeTriad newNode) {

    }

    @Override
    public FindValueResult findValue(String fileId) {
        return null;
    }

    @Override
    protected boolean isCompatibleVersion(int version) {
        return false;
    }

    @Override
    protected String getVersion() {
        return null;
    }

    @Override
    protected int getPersistenceNodeSize() {
        return -1;
    }

}
