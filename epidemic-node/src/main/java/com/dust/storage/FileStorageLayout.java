package com.dust.storage;

import com.dust.NodeConfig;
import com.dust.fundation.EpidemicUtils;
import com.dust.grpc.kademlia.StoreRequest;
import com.dust.logs.LogFormat;
import com.dust.logs.Logger;
import com.dust.storage.btree.BTreeManager;
import com.dust.storage.btree.DataNode;
import com.google.protobuf.ByteString;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

public class FileStorageLayout extends StorageLayout {

    /**
     * 存储相关的两个文件后缀名
     */
    public static final String MD_SUFFIX = ".md";
    public static final String DATA_SUFFIX = ".data";

    //可写入的元数据文件操作对象
    private RandomAccessFile writeMD;
    //与元数据文件操作对象相对应的数据写入对象
    private RandomAccessFile writeData;
    //文件操作对象的对象名称
    private String writeName;

    /**
     * 文件目录
     */
    private final BTreeManager catalog;

    public FileStorageLayout(NodeConfig config) throws IOException {
        super(config);
        catalog = BTreeManager.create(config.getOrderNum());
    }

    @Override
    public void load() throws IOException {
        File dir = new File(storagePath);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new IOException("创建文件夹失败：" + dir.getPath());
            }
            return;
        }

        //读取所有元数据文件
        File[] mdFiles = dir.listFiles((f, name) -> name.endsWith(MD_SUFFIX));
        if (Objects.isNull(mdFiles) || mdFiles.length == 0) {
            String fileName = EpidemicUtils.randomNodeId(config.getNodeSalt());
            initFileIO(fileName);
            return;
        }

        for (File mdFile : mdFiles) {
            String fileName = mdFile.getName();
            RandomAccessFile raf = new RandomAccessFile(mdFile, "r");
            raf.seek(0);
            final FileChannel channel = raf.getChannel();
            try (raf; channel) {
                while (raf.getFilePointer() < raf.length()) {
                    DataNode dataNode = DataNode.byFile(raf, fileName);
                    if (Objects.nonNull(dataNode))
                        catalog.insert(dataNode);
                }
            }
        }

        //选取最小的MD文件作为当前活动文件
        File minFile = Arrays.stream(mdFiles)
                .min(Comparator.comparingLong(File::length))
                .get();
        String fileName = minFile.getName();
        initFileIO(fileName);

//        printMetadata();
    }

    /**
     * 打印元数据
     */
    private void printMetadata() {
        var iter = catalog.iterator();
        while (iter.hasNext()) {
            var node = iter.next();
            System.out.println(node.toString());
        }
    }

    /**
     * 文件SHA1ID初始化本地存储文件读取对象
     * @param fileName SHA1id
     * @throws FileNotFoundException
     *  找不到文件
     */
    private void initFileIO(String fileName) throws FileNotFoundException {
        if (fileName.endsWith(".md") || fileName.endsWith(".data")) {
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
        }
        writeName = fileName;
        writeMD = new RandomAccessFile(new File(storagePath, fileName + MD_SUFFIX), "rw");
        writeData = new RandomAccessFile(new File(storagePath, fileName + DATA_SUFFIX), "rw");
    }

    @Override
    public Optional<ByteBuffer> find(String fileId) throws IOException {
        var node = catalog.find(fileId);
        if (Objects.isNull(node)) {
            return Optional.empty();
        }
        return Optional.of(read(node));
    }

    /**
     * 根据dataNode对象读取对应的文件信息
     * @param node 要读取的文件元数据
     * @return 如果存在则返回，否则返回null
     */
    private ByteBuffer read(DataNode node) throws IOException {
        String fileName = node.getDataName() + DATA_SUFFIX;
        var raf = new RandomAccessFile(new File(storagePath, fileName), "r");
        final var channel = raf.getChannel();
        ByteBuffer result = ByteBuffer.allocate((int) node.getSize());
        try (raf; channel) {
            raf.seek(node.getOffset());
            channel.read(result);
        }
        result.flip();
        return result;
    }

    /**
     * 单线程存入
     */
    @Override
    public synchronized void store(StoreRequest storeRequest) throws IOException {
        String fileId = storeRequest.getFileId();
        ByteString reqData = storeRequest.getData();

        writeMD.seek(writeMD.length());
        writeData.seek(writeData.length());

        long size = reqData.size();
        long mdOffset = writeMD.length();
        long offset = writeData.length();

        ByteBuffer data = reqData.asReadOnlyByteBuffer();
        final FileChannel channel = writeData.getChannel();
        channel.write(data);

        DataNode dataNode = catalog.insert(fileId);
        dataNode.load((byte) 0, writeName, offset, size, mdOffset);
        dataNode.toFile(writeMD);
        System.out.println("store");
        Logger.storageLog.info(LogFormat.STORAGE_SAVE_FORMAT, fileId, size, writeName, offset);
        if (writeData.length() > chunkSize) {
            freezeAndCreate();
        }
    }

    /**
     * 冻结当前文件块，并创建一个新的文件块
     */
    public void freezeAndCreate() throws IOException {
        writeMD.close();
        writeData.close();

        String newName = EpidemicUtils.randomNodeId(config.getNodeSalt());
        writeMD = new RandomAccessFile(new File(storagePath, newName + MD_SUFFIX), "rw");
        writeData = new RandomAccessFile(new File(storagePath, newName + DATA_SUFFIX), "rw");
        writeName = newName;
    }

    @Override
    public synchronized boolean delete(String fileId) throws IOException {
        return false;
    }

    @Override
    protected boolean isCompatibleVersion(long version) {
        return true;
    }

    @Override
    protected long getVersion() {
        return 1l;
    }

}
