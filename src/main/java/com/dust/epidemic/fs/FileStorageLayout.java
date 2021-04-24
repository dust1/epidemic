package com.dust.epidemic.fs;

import com.dust.epidemic.core.EpidemicConfig;
import com.dust.epidemic.foundation.LRUCache;
import com.dust.epidemic.foundation.OutputUtils;
import com.dust.epidemic.foundation.buffer.ReusableBuffer;
import com.dust.epidemic.fs.checksum.ChecksumAlgorithm;
import com.dust.epidemic.fs.entity.WriteResult;
import com.dust.epidemic.net.common.StripingPolicyImpl;
import com.dust.epidemic.net.rpc.RPCconstant;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Objects;

/**
 * 文件存储实现
 */
public class FileStorageLayout extends StorageLayout {

    /**
     * 当前存储系统版本号
     */
    private static final int SL_TAG = 0x0100001;

    /**
     * 元数据记录大小：16字节
     */
    private static final int MDRECORD_SIZE = Long.SIZE / 8 * 2;

    /**
     * 数据存储文件后缀名
     */
    private static final String DATA_SUFFIX = ".data";

    /**
     * 数据存储文件中对应的元数据文件后缀名
     */
    private static final String MD_SUFFIX   = ".md";

    /**
     *
     */
    private static final String TEPOCH_SUFFIX = ".te";

    /**
     * 在元数据处理器集合中，数据对象所在的处理器下标
     */
    private static final int DATA_HANDLE = 0;

    /**
     * 在元数据处理器集合中，元数据文件所在的处理器下标
     */
    private static final int MD_HANNDLE = 1;

    /**
     * 是否开启校验和
     */
    private final boolean checkSumEnabled;

    /**
     * 元数据存储空间
     */
    private final ByteBuffer mdate;

    /**
     * 路径缓存
     */
    private final LRUCache<String, String> hashedPathCache;

    /**
     * 校验和算法对象
     * 如哈希存储不需要用到校验和，而文件与对象可能用到校验和
     */
    private ChecksumAlgorithm checksumAlgorithm;

    public FileStorageLayout(EpidemicConfig config, MetadataCache cache) throws IOException {
        super(config, cache);
        this.checkSumEnabled = config.isUseCheckSum();
        this.mdate = ByteBuffer.allocate(MDRECORD_SIZE);

        if (checkSumEnabled) {
            //TODO 初始化计算校验和的算法类
            //this.checksumAlgorithm = ChecksumFactory.getInstance(...);
        }

        this.hashedPathCache = new LRUCache<>(2048);

        //TODO 写入日志
        //...
    }

    @Override
    public ObjectInformation readObject(String fileId, FileMetadata md, long objNo, int offset, int length, long version) throws IOException {
        return null;
    }

    @Override
    public void writeObject(String fileId, FileMetadata md, ReusableBuffer data, long objNo, int offset, long newVersion, boolean sync) throws IOException {

    }

    @Override
    public WriteResult writeObject(ReusableBuffer data, boolean sync) throws IOException {
        return null;
    }

    @Override
    public void deleteObject(String fileId, FileMetadata md, long objNo, long version) throws IOException {

    }

    @Override
    public FileMetadata splitFile(String fileId, FileMetadata md, long splitOffset, long newVersion, boolean sync) throws IOException {
        return null;
    }

    @Override
    public void deleteFile(String fileId, boolean deleteMetadata) throws IOException {

    }

    /**
     * TODO 问题：
     */
    @Override
    protected FileMetadata loadFileMetadata(String fileId, StripingPolicyImpl sp) throws IOException {
        FileMetadata md = new FileMetadata(sp);
        md.initObjectMap();
        if (checkSumEnabled) {
            md.initObjectChecksum();
        }

        File f = new File(getFilePath(fileId) + DATA_SUFFIX);
        if (!f.exists()) {
            md.setGlobalLastObjectNumber(-1);
            md.setLargestObjectVersion(-1);
            md.setLatestObjectVersion(-1);
            md.setFileSize(0);
            return md;
        }

        openHandles(md, fileId);
        RandomAccessFile ofile = md.getHandles()[DATA_HANDLE];
        RandomAccessFile mdfile = md.getHandles()[MD_HANNDLE];

        //从0开始读取
        mdfile.seek(0);

        //为什么要调用这个方法？
        //是为了获取这个文件的读写锁
        FileChannel mdChannel = mdfile.getChannel();

        /*
         * 根据文件大小和每个对象的大小计算出这个文件中对象的数量
         */
        final long fileSize = ofile.length();
        final long stripeSize = sp.getStripeSizeForObject(0);
        //一个文件中对象的个数
        final long numObjs = (long) Math.ceil((double) fileSize / (double) stripeSize);
        for (int i = 0; i < numObjs; i++) {
            long objNo = sp.getGloablObjectNumber(i);

            long chkSum = mdfile.readLong();
            long version = mdfile.readLong();
            if (version == 0)
                continue;
            if (checkSumEnabled)
                md.updateObjectChecksum(objNo, chkSum, version);
            md.updateObjectVersion(objNo, version);
        }
        md.setLastObjectNumber(sp.getGloablObjectNumber(numObjs - 1));

        //获取全局最后一个对象号、分块大小、文件长度
        md.setFileSize((md.getGlobalLastObjectNumber() - 1) * stripeSize + fileSize % stripeSize);

        File tepoch = new File(getFilePath(fileId) + TEPOCH_SUFFIX);
        if (tepoch.exists()) {
            RandomAccessFile rf = new RandomAccessFile(tepoch, "r");
            md.setTruncateEpoch(rf.readLong());
            rf.close();
        }

        md.setGlobalLastObjectNumber(-1);
        return md;
    }

    /**
     * 打开指定文件及其元数据文件的随机读写器，并保存到元数据对象中
     * @param md 保存的元数据对象
     * @param fileId 要打开的文件ID
     * @throws IOException
     *      如果打开文件失败，即获取文件的读写锁失败
     */
    private void openHandles(FileMetadata md, String fileId) throws IOException {
        if (Objects.isNull(md.getHandles())) {
            String fileName = getFilePath(fileId);
            //TODO 此处可以打印日志，表明打开了哪个文件
            RandomAccessFile ofile = new RandomAccessFile(fileName + DATA_SUFFIX, "rw");
            RandomAccessFile mdfile = new RandomAccessFile(fileName + MD_SUFFIX, "rw");
            RandomAccessFile[] handles = {ofile, mdfile};
            md.setHandles(handles);
        }
    }

    /**
     * 根据fileId获取.data文件路径
     * @param fileId .data抽象的fileId
     * @return 返回fileId对应的.data文件路径
     */
    private String getFilePath(String fileId) {
        //格式/dir/xxx/fileId

        String path = hashedPathCache.get(fileId);
        if (!hashedPathCache.containsKey(fileId)) {
            StringBuilder hashPath = new StringBuilder(saveDir.length() + fileId.length() + 6);
            hashPath.append(saveDir);

            int hashId = fileId.hashCode();
            hashPath.append(OutputUtils.trHex[hashId & 0x0f]);
            hashPath.append(OutputUtils.trHex[hashId >> 4 & 0x0f]);
            hashPath.append("/");
            hashPath.append(OutputUtils.trHex[hashId >> 8 & 0x0f]);
            hashPath.append(OutputUtils.trHex[hashId >> 12 & 0x0f]);
            hashPath.append("/");

            String dir = hashPath.toString();
            File f = new File(dir);
            if (!f.exists()) {
                f.mkdirs();
            }

            hashPath.append(fileId);
            path = hashPath.toString();
            hashedPathCache.put(fileId, path);
        }
        return path;
    }

    @Override
    public boolean fileExists(String fileId) {
        return false;
    }

    @Override
    protected int getLayoutVersionTag() {
        return 1;
    }

    @Override
    protected boolean isCompatibleVersion(int version) {
        return version == SL_TAG;
    }

}
