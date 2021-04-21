package com.dust.epidemic.fs;

import java.util.Map;

/**
 * 文件元数据对象
 * 记录了.data文件的历史版本号、各个object的校验和、文件大小、object对象数量、随机读写对象以及元数据长度
 */
public class FileMetadata {

    /**
     * 获取最大的版本号，但是这个版本号并不一定是完整的，
     * 可能是另一个线程写入到一半的版本，读取时会出现数据错误
     * @return 最大的版本号
     */
    public long getLargestObjectVersion() {
        //TODO 获取最大的版本号
        return 0;
    }

    /**
     * 获取最新的版本号，这个版本号一定是数据完成写入的版本号，
     * 读取时不会出现数据缺失、不一致等现象
     * @return 最后一次提交的版本
     */
    public long getLatestObjectVersion() {
        return 0;
    }

    /**
     * 根据objNo获取这个对象最后一次提交版本的版本号
     * @param objNo 查询的objNo
     * @return 如果这个对象不存在则返回-1
     */
    public long getLatestObjectVersionByObj(long objNo) {
        //TODO
        return -1;
    }

}
