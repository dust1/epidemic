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
            bucket = new KademliaBucket(config.getBucketKey(), myId);
            return;
        }

        if (Objects.isNull(snapshot)) {
            snapshot = new RandomAccessFile(f, "rw");
        }
        snapshot.seek(0);
        //尝试获取文件的读写锁
        final FileChannel fileChannel = snapshot.getChannel();

        int index = 0;
        while (index < HEAD.length && snapshot.getFilePointer() < snapshot.length()) {
            if (snapshot.readByte() == HEAD[index]) {
                index += 1;
            } else {
                index = 0;
            }
        }
        if (index < HEAD.length) {
            snapshot.seek(0);
            fileChannel.close();
            return;
        }
        byte[] myNodeDatas = new byte[40];
        snapshot.readFully(myNodeDatas);
        this.myId = new String(myNodeDatas, StandardCharsets.UTF_8);
        this.bucket = new KademliaBucket(config.getBucketKey(), myId);

        while (snapshot.getFilePointer() < snapshot.length()) {
            var node = NodeTriadRouterNode.fromFile(snapshot);
            if (Objects.isNull(node)) {
                break;
            }
            bucket.add(node);
        }
        fileChannel.close();
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
