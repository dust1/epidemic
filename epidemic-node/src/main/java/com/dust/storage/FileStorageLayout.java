package com.dust.storage;

import com.dust.NodeConfig;
import com.dust.fundation.EpidemicUtils;
import com.dust.grpc.kademlia.KademliaServiceGrpc;
import com.dust.grpc.kademlia.NodeInfo;
import com.dust.grpc.kademlia.StoreRequest;
import com.dust.grpc.kademlia.StoreResponse;
import com.dust.logs.LogFormat;
import com.dust.logs.Logger;
import com.dust.scheduler.RePublishing;
import com.dust.scheduler.RePublishingTask;
import com.dust.storage.btree.BTreeManager;
import com.dust.storage.btree.DataNode;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannelBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class FileStorageLayout extends StorageLayout {

    /**
     * 存储相关的两个文件后缀名
     */
    public static final String MD_SUFFIX = ".md";
    public static final String DATA_SUFFIX = ".da";

    /**
     * 文件操作对象的对象名称
     */
    private String writeName;

    /**
     * 文件目录
     */
    private final BTreeManager catalog;

    private final String nodeId;

    private final RePublishing rePublishing;

    public static StorageLayout create(NodeConfig config, String nodeId) throws IOException {
        String tmp = config.getStoragePath();
        if (!tmp.endsWith("/")) {
            tmp += "/";
        }
        byte[] head = {0xd, 0xa, 0xd, 0xa};

        var rePublishing = new RePublishing(config);
        return new FileStorageLayout(tmp, config, 1L, ".storage_version", head, nodeId, rePublishing);
    }

    private FileStorageLayout(String path, NodeConfig config, long version,
                             String versionFileName, byte[] head, String nodeId,
                              RePublishing rePublishing) throws IOException {
        super(path, config, version, versionFileName, head);
        this.nodeId = nodeId;
        this.catalog = BTreeManager.create(config.getOrderNum());
        this.rePublishing = rePublishing;
    }

    @Override
    public void before() throws IOException {
        File dir = new File(path);
        //读取所有元数据文件
        File[] mdFiles = dir.listFiles((f, name) -> name.endsWith(MD_SUFFIX));
        if (Objects.isNull(mdFiles) || mdFiles.length == 0) {
            //不存在元数据则随机生成一个待写入文件id
            this.writeName = EpidemicUtils.randomNodeId(config.getNodeSalt());
            return;
        }

        File minFile = mdFiles[0];
        for (File mdFile : mdFiles) {
            if (mdFile.length() < minFile.length()) {
                minFile = mdFile;
            }

            String fileName = mdFile.getName();
            String fileId = fileName.substring(0, fileName.lastIndexOf(MD_SUFFIX));

            RandomAccessFile raf = new RandomAccessFile(mdFile, "r");
            raf.seek(0);
            final FileChannel channel = raf.getChannel();
            try (raf; channel) {
                while (raf.getFilePointer() < raf.length()) {
                    DataNode dataNode = DataNode.byFile(raf, fileId, head);
                    if (Objects.nonNull(dataNode))
                        catalog.insert(dataNode);
                }
            }
        }

        String fileName = minFile.getName();
        this.writeName = fileName.substring(0, fileName.lastIndexOf(MD_SUFFIX));
    }

    @Override
    public ByteBuffer find(String fileId) throws IOException {
        var node = catalog.find(fileId);
        if (Objects.isNull(node)) {
            var buffer = ByteBuffer.allocate(0);
            buffer.flip();
            return buffer;
        }
        return readByDataNode(node);
    }

    /**
     * 根据dataNode对象读取对应的文件信息
     * @param node 要读取的文件元数据
     * @return 如果存在则返回，否则返回null
     */
    private ByteBuffer readByDataNode(DataNode node) throws IOException {
        String fileName = node.getDataName() + DATA_SUFFIX;
        var raf = new RandomAccessFile(new File(path, fileName), "r");
        final var channel = raf.getChannel();
        ByteBuffer result = ByteBuffer.allocate((int) node.getSize());
        try (raf; channel) {
            raf.seek(node.getOffset());
            channel.read(result);
        }
        result.flip();
        return result;
    }

    @Override
    public void store(ByteBuffer buffer, String fileId) throws IOException {
        var data = catalog.find(fileId);
        if (Objects.isNull(data)) {
            //TODO 该文件存在，更新它的republish时间
            return;
        }

        var mdFile = new RandomAccessFile(new File(path, writeName + MD_SUFFIX), "rw");
        var dataFile = new RandomAccessFile(new File(path, writeName + DATA_SUFFIX), "rw");
        try (mdFile; dataFile;
             final var channel = dataFile.getChannel()) {
            mdFile.seek(mdFile.length());
            dataFile.seek(dataFile.length());
            long size = buffer.limit();
            long mdOffset = mdFile.length();
            long offset = dataFile.length();

            channel.write(buffer);

            DataNode dataNode = catalog.insert(fileId);
            dataNode.load((byte) 0, writeName, offset, size, mdOffset);
            dataNode.toFile(mdFile, head);
            Logger.storageLog.info(LogFormat.STORAGE_SAVE_FORMAT, fileId, size, writeName, offset);
            if (dataFile.length() > chunkSize) {
                this.writeName = EpidemicUtils.randomNodeId(config.getNodeSalt());
            }
        }
    }

    /**
     * 当节点收到一个新的节点请求后将这个请求传递给storageLayout用于检查是否有文件距离该节点更近。
     * 如果有则创建Re-Publishing任务放入待传输队列中
     *
     * 假设一个key-value对向集群中k个节点发起store rpc。
     * 当节点收到某个key-value对的store rpc时，它可以认为该rpc也发给了其他k-1个其他节点，
     * 因此在下一个小时就不会重新发布该key-value对。
     * 由于间隔时间长，保证了每个节点重新发布的时间都不会是同步的，
     * 可以尽量保证对于k个节点，每个时间点都只会有1个节点重新发布
     * @param newNodeId 传入节点的id
     * @param host 节点host
     * @param port 节点端口号
     */
    @Override
    public void haveNewNode(String newNodeId, String host, int port) {
        if (newNodeId.equals(nodeId)) {
            return;
        }

        var iter = catalog.iterator();
        var pushNodes = new ArrayList<DataNode>();

        //筛选距离新节点更近的文件
        while (iter.hasNext()) {
            var node = iter.next();
            int mDis = EpidemicUtils.getDis(node.getFileId(), nodeId);
            int newDis = EpidemicUtils.getDis(node.getFileId(), newNodeId);
            if (mDis >= newDis) {
                pushNodes.add(node);
            }
        }

        //将新节点加入等待队列中
        pushNodes.forEach(node -> rePublishing.push(RePublishingTask.create(node.getDataName(),
                node.getOffset(), node.getSize(), host, port)));
    }

    @Override
    public synchronized boolean delete(String fileId) throws IOException {
        return false;
    }

    @Override
    public boolean isCompatibleVersion(long version) {
        return this.version == version;
    }

}
