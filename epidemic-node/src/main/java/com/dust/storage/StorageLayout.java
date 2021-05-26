package com.dust.storage;

import com.dust.BaseLayout;
import com.dust.NodeConfig;
import com.dust.fundation.EpidemicUtils;
import com.dust.grpc.kademlia.StoreRequest;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.InvalidParameterException;
import java.util.Objects;
import java.util.Optional;

public abstract class StorageLayout extends BaseLayout {

    /**
     * 存储文件块的限制大小
     */
    protected long chunkSize;

    StorageLayout(String path, NodeConfig config, long version,
                            String versionFileName, byte[] head) throws IOException {
        super(path, version, versionFileName, head, config);
        this.chunkSize = config.getChunkSize();
        if (chunkSize <= 0) {
            throw new InvalidParameterException("快照文件大小参数异常，错误的或者是不支持的大小配置:" + config.getChunkSize());
        }
    }

    /**
     * 从文件目录中根据文件id查询文件，如果存在则返回文件内容
     * @param fileId 要查询的文件id
     * @return 如果存在则返回文件内容，否则为空.
     * @throws IOException
     *      读取失败
     */
    public abstract ByteBuffer find(String fileId) throws IOException;

    /**
     * 保存数据
     * @throws IOException
     *      如果保存失败
     */
    public abstract void store(ByteBuffer buffer, String fileId) throws IOException;

    /**
     * 根据文件id删除对应的文件
     * 这里的删除只是逻辑删除，真正磁盘上的删除需要等到垃圾回收之后
     * @param fileId 要删除的文件id
     * @return 如果删除成功则返回true，否则返回false
     * @throws IOException
     *      磁盘读写失败
     */
    public abstract boolean delete(String fileId) throws IOException;

}
