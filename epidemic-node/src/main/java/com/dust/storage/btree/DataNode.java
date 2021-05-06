package com.dust.storage.btree;

import com.dust.storage.StorageLayout;
import com.dust.storage.buffer.ReusableBuffer;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * B树目录中数据的存储节点，用于维护数据本身以及相关描述
 *
 * 数据并不会保存在这个对象中，这个对象持有对本地数据的引用
 * 对接着fs与目录树
 *
 * 就是文件的虚拟对象，同时承担着文件读写入口的工作
 *
 * 每个DataNode只会对应一个文件块
 */
@Getter
@Setter
public class DataNode {

    //初始化完成，但还无法开始工作。因为还没有载入存储系统
    private static final int INIT       = 0;

    //已经载入存储系统，可以开始工作
    private static final int WORKING    = 1;

    //要被释放，无法继续提供服务
    private static final int DESTROY    = 2;

    /**
     * 持久化数据的文件开头
     */
    private static final byte[] HEAD = {0xd, 0xa, 0xd, 0xe};

    /**
     * 文件虚拟对象的状态
     */
    private int working;

    /**
     * 文件id
     */
    private String fileId;

    /**
     * 文件所在的.data文件名称
     */
    private String dataName;

    /**
     * 文件所在偏移地址
     */
    private long offset;

    /**
     * 文件大小
     */
    private long size;

    private DataNode(String fileId) {
        this.fileId = fileId;
        this.working = INIT;
    }

    private DataNode(String fileId, String dataName, long offset, long size) {
        this.fileId = fileId;
        this.dataName = dataName;
        this.offset = offset;
        this.size = size;
        this.working = WORKING;
    }

    public static DataNode byFileId(String fileId) {
        return new DataNode(fileId);
    }

    /**
     * 根据持久化文件创建DataNode对象
     * @param raf 文件句柄
     * @return 创建的文件呢元数据对象
     */
    public static DataNode byFile(RandomAccessFile raf) throws IOException {
        FileChannel channel = raf.getChannel();
        int index = 0;
        while (index < HEAD.length && raf.getFilePointer() < raf.length()) {
            if (raf.readByte() == HEAD[index]) {
                index += 1;
            } else {
                index = 0;
            }
        }
        if (index < HEAD.length)
            return null;

        byte[] keyData = new byte[40];
        raf.readFully(keyData);
        String key = new String(keyData, StandardCharsets.UTF_8);

        raf.readFully(keyData);
        String fileName = new String(keyData, StandardCharsets.UTF_8) + ".data";

        long offset = raf.readLong();
        long fileSize = raf.readLong();

        return new DataNode(key, fileName, offset, fileSize);
    }

    /**
     * 将给定的DataNode对象的所有信息都转储到自身
     * @param dataNode 需要被复制的数据来源对象
     */
    public void loadByDataNode(DataNode dataNode) {
        if (Objects.isNull(dataNode))
            return;

        this.fileId = dataNode.getFileId();
        this.dataName = dataNode.getDataName();
        this.offset = dataNode.getOffset();
        this.size = dataNode.getSize();
        this.working = WORKING;
    }

    /**
     * 写入数据
     * 由于文件数据通过RPC传输，采用GRPC传输，一次传输并创建一个文件块。即每个DataNode只能被写入一次
     * @return 如果写入失则返回false，否则返回true
     */
    public boolean write(ReusableBuffer data) {
//        checkStatus();
//        WriteResult writeResult;
//        try {
//            writeResult = storageLayout.writeObject(data, true);
//        } catch (IOException e) {
//            //TODO 写入文件失败，记录日志
//            return null;
//        }
//        assert Objects.nonNull(writeResult);
//        dataInfo.load(writeResult);
//
//        return encode(writeResult);
        return false;
    }

    /**
     * 检查当前节点能否正常提供服务
     */
    private void checkStatus() {
        if (working != WORKING) {
            throw new RuntimeException("当前的DataNode状态已经无法提供正常读写服务。当前状态码：" + working);
        }
    }

    /**
     * 释放文件空间
     * 这里的释放只是进行标记删除，物理删除需要等到垃圾回收机制启动
     */
    public boolean destroy() {
//        if (working == INIT) {
//            //这个节点并没有关联到物理磁盘，直接返回，等待GC将其回收
//            return true;
//        }
//        working = DESTROY;


        //TODO 释放磁盘空间
        return false;
    }

}
