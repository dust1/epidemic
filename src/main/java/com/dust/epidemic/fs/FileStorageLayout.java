package com.dust.epidemic.fs;

import com.dust.epidemic.core.EpidemicConfig;
import com.dust.epidemic.foundation.LRUCache;
import com.dust.epidemic.foundation.buffer.ReusableBuffer;
import com.dust.epidemic.fs.checksum.ChecksumAlgorithm;
import com.dust.epidemic.fs.entity.WriteResult;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * 文件存储实现
 */
@Slf4j
public class FileStorageLayout extends StorageLayout {

    /**
     * 元数据记录大小：16字节
     */
    private static final int MDRECORD_SIZE = Long.SIZE / 8 * 2;

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
    public void deleteObject(String fileId, FileMetadata md, long objNo, long version) throws IOException {

    }

    @Override
    public FileMetadata splitFile(String fileId, FileMetadata md, long splitOffset, long newVersion, boolean sync) throws IOException {
        return null;
    }

    @Override
    public void deleteFile(String fileId, boolean deleteMetadata) throws IOException {

    }

    @Override
    protected FileMetadata loadFileMetadata(String fileId) throws IOException {
        return null;
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
        // 默认都兼容
        return true;
    }

}
