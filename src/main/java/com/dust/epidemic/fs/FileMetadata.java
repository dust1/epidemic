package com.dust.epidemic.fs;

import com.dust.epidemic.net.common.StripingPolicyImpl;

import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件元数据对象
 * 记录了.data文件的历史版本号、各个object的校验和、文件大小、object对象数量、随机读写对象以及元数据长度
 */
public class FileMetadata {

    /**
     * 最新版本号
     */
    private long largestObjectVersion;

    private long latestObjectVersion;

    private Map<Long, Long> largestObjectMap;

    private Map<Long, Long> latestObjectMap;

    private Map<Long, Map<Long, Long>> checkSumMap;

    public FileMetadata(StripingPolicyImpl sp) {

    }

    public void initObjectMap() {
        this.largestObjectMap = new HashMap<>();
        this.latestObjectMap = new HashMap<>();
    }

    public void initObjectChecksum() {
        this.checkSumMap = new HashMap<>();
    }

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

    public RandomAccessFile[] getHandles() {
        return null;
    }

    /**
     * 获取这个文件最后一个对象的对象编号
     * @return 这个文件最后一个对象的编号
     */
    public long getGlobalLastObjectNumber() {
        return -1;
    }

    public void setLargestObjectVersion(long version) {

    }

    public void setLatestObjectVersion(long version) {

    }

    public void setFileSize(long size) {

    }

    public void setGlobalLastObjectNumber(long objNo) {

    }

    public void setHandles(RandomAccessFile[] handles) {

    }

    /**
     * 设置这个文件中位于文件末最后一个对象编号
     * @param objNo 最后一个对象编号
     */
    public void setLastObjectNumber(long objNo) {

    }

    public void setTruncateEpoch(long te) {

    }

    /**
     * 更新元数据对象中给定对象id的指定版本的校验和
     * @param objNo 要更新的对象id
     * @param checkSum 要更新的校验和
     * @param version 要更新的版本号
     */
    public void updateObjectChecksum(long objNo, long checkSum, long version) {

    }

    /**
     * 更新元数据对象中给定对象id的版本号
     * @param objNo 要更新的对象id
     * @param version 要更新的版本号
     */
    public void updateObjectVersion(long objNo, long version) {

    }

}
