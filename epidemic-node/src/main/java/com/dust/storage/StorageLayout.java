package com.dust.storage;

import com.dust.core.Layout;
import com.dust.core.NodeConfig;
import com.dust.grpc.kademlia.StoreRequest;
import com.dust.grpc.kademlia.StoreResponse;
import com.dust.storage.btree.DataNode;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.InvalidParameterException;
import java.util.Objects;
import java.util.Optional;

public abstract class StorageLayout extends Layout {

    protected NodeConfig config;

    /**
     * 文件保存目录
     */
    protected String saveDir;

    /**
     * 存储文件块的限制大小
     */
    protected long chunkSize;

    protected StorageLayout(NodeConfig config) {
        this.saveDir = config.getSaveDir();
        this.chunkSize = strSizeToLong(config.getChunkSize());
        if (chunkSize <= 0) {
            throw new InvalidParameterException("快照文件大小参数异常:" + config.getChunkSize());
        }
    }

    /**
     * 字符串的快照文件大小转为long长度
     * @param size 配置文件中的长度
     * @return 转换后以字节为单位
     */
    private long strSizeToLong(String size) {
        if (Objects.isNull(size) || size.isBlank()) {
            //未配置参数
            return 0;
        }
        if (size.length() <= 2) {
            //参数配置错误
            return -1;
        }
        return 1;
    }

    /**
     * 加载元数据文件并构建文件目录
     * 文件目录由B+树组成
     * @throws IOException
     *      读取元数据文件失败
     */
    public abstract void load() throws IOException;

    /**
     * 从文件目录中根据文件id查询文件，如果存在则返回文件内容
     * @param fileId 要查询的文件id
     * @return 如果存在则返回文件内容，否则为空.
     * @throws IOException
     *      读取失败
     */
    public abstract Optional<ByteBuffer> find(String fileId) throws IOException;

    /**
     * 保存数据
     * @throws IOException
     *      如果保存失败
     */
    public abstract void store(StoreRequest storeRequest) throws IOException;

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
