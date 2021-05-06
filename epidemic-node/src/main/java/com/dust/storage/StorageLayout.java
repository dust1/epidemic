package com.dust.storage;

import com.dust.core.Layout;
import com.dust.core.NodeConfig;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Objects;

public abstract class StorageLayout extends Layout {

    protected NodeConfig config;

    /**
     * 文件保存目录
     */
    protected String saveDir;

    /**
     * 快照文件的大小
     */
    protected long snapshotSize;

    protected StorageLayout(NodeConfig config) {
        this.saveDir = config.getSaveDir();
        this.snapshotSize = strSizeToLong(config.getSnapshotSize());
        if (snapshotSize <= 0) {
            throw new InvalidParameterException("快照文件大小参数异常:" + config.getSnapshotSize());
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

}
