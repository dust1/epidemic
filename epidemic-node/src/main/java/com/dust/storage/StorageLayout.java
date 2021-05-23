package com.dust.storage;

import com.dust.NodeConfig;
import com.dust.fundation.EpidemicUtils;
import com.dust.grpc.kademlia.StoreRequest;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.InvalidParameterException;
import java.util.Objects;
import java.util.Optional;

public abstract class StorageLayout {

    /**
     * 存储模块的版本文件名称
     */
    public static final String VERSION_FILENAME = ".storage_version";

    protected NodeConfig config;

    /**
     * 文件保存目录
     */
    protected String storagePath;

    /**
     * 存储文件块的限制大小
     */
    protected long chunkSize;

    protected StorageLayout(NodeConfig config) throws IOException {
        this.config = config;
        String tmp = config.getStoragePath();
        if (!tmp.endsWith("/")) {
            tmp += "/";
        }
        EpidemicUtils.checkAndWriteVersion(tmp,
                VERSION_FILENAME, this::isCompatibleVersion,
                getVersion());
        this.storagePath = config.getStoragePath();
        this.chunkSize = strSizeToLong(config.getChunkSize());
        if (chunkSize <= 0) {
            throw new InvalidParameterException("快照文件大小参数异常，错误的或者是不支持的大小配置:" + config.getChunkSize());
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
        size = size.toUpperCase();
        String tmp = size.substring(0, size.length() - 2);;
        if (size.endsWith("KB")) {
            return Integer.parseInt(tmp) * 1024L;
        } else if (size.endsWith("MB")) {
            return Integer.parseInt(tmp) * 1024L * 1024L;
        } else if (size.endsWith("GB")) {
            return Integer.parseInt(tmp) * 1024L * 1024L * 1024L;
        }
        return -1;
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

    /**
     * 存储节点根据nodeId检查自身文件是否有距离该id更近的情况
     * 如果存在则将文件发送给这个节点。
     * @param nodeId 节点id
     * @param host 节点host
     * @param myId 当前节点自身的id
     * @param port 节点端口
     */
    public void ping(String nodeId, String myId, String host, int port) {
        //do not anything
    }

    /**
     * 检查当前的实现版本与给定的版本是否兼容
     * @param version 检查的版本
     * @return 如果兼容则返回true，否则返回false
     */
    protected abstract boolean isCompatibleVersion(long version);

    /**
     * 获取当前系统的实现版本
     * @return 当前系统版本的string形式
     */
    protected abstract long getVersion();

}
