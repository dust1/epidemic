package com.dust.storage.btree;

import com.dust.fundation.EpidemicUtils;
import com.dust.logs.LogFormat;
import com.dust.logs.Logger;
import com.dust.storage.FileStorageLayout;
import com.dust.storage.StorageLayout;
import com.dust.storage.buffer.ReusableBuffer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
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
@ToString
public class DataNode {

    /**
     * 文件虚拟对象的状态
     */
    private int status;

    /**
     * 文件id
     */
    private String fileId;

    /**
     * 逻辑删除标志
     */
    private byte deleted;

    /**
     * 文件类型
     * 0：数据文件
     *  - 数据文件的文件内容是文件本身信息
     * 1：质子文件
     *  - 文件内容是这个文件保存的三元组信息【1.0版本暂不实现】
     */
    private byte type;

    /**
     * 文件所在的.data文件名称
     * 不携带.data后缀
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

    /**
     * 元数据所在.md文件的偏移地址
     */
    private long mdOffset;

    private DataNode(String fileId) {
        this.fileId = fileId;
        this.status = 0;
    }

    private DataNode(String fileId, byte deleted, byte type,
                     String dataName, long offset, long size,
                     long mdOffset) {
        this.fileId = fileId;
        this.deleted = deleted;
        this.type = type;
        this.dataName = dataName;
        this.offset = offset;
        this.size = size;
        this.mdOffset = mdOffset;
        this.status = 1;
    }

    public static DataNode byFileId(String fileId) {
        return new DataNode(fileId);
    }

    /**
     * 根据持久化文件创建DataNode对象
     * @param raf 文件句柄
     * @param dataId 元数据所在的文件名,不带后缀
     * @return 创建的文件呢元数据对象
     */
    public static DataNode byFile(RandomAccessFile raf, String dataId, byte[] head) throws IOException {
        long mdOffset = raf.getFilePointer();
        if (!EpidemicUtils.checkHead(head, raf)) {
            return null;
        }

        String key = EpidemicUtils.readToSHA1(raf);

        byte deleted = raf.readByte();
        byte type = raf.readByte();

        long offset = raf.readLong();
        long fileSize = raf.readLong();

        return new DataNode(key, deleted, type, dataId,
                offset, fileSize, mdOffset);
    }

    /**
     * 加载DataNode对象，这个方法通常在{@code DataNode.byFileId()}创建对象之后调用。
     * 传入的是存储过程中产生的各种元数据信息
     */
    public void load(byte type, String dataName, long offset,
                     long size, long mdOffset) {
        this.deleted = 0;
        this.type = type;
        this.dataName = dataName;
        this.offset = offset;
        this.size = size;
        this.mdOffset = mdOffset;
        this.status = 1;
    }

    /**
     * 将这个DataNode持久化到磁盘中
     * @param raf 要写入的元数据文件的句柄。该句柄的写入为单线程
     */
    public void toFile(RandomAccessFile raf, byte[] head) throws IOException {
        //持久化过程中不能对外提供服务
        status = 0;
        raf.write(head);
        raf.write(fileId.getBytes(StandardCharsets.UTF_8));
        raf.writeByte(deleted);
        raf.writeByte(type);
        raf.writeLong(offset);
        raf.writeLong(size);
        status = 1;
    }

    /**
     * 删除自身
     */
    public void delete(RandomAccessFile raf, byte[] head) throws IOException {
        this.deleted = 1;
        editDelete(raf, head, (byte) 1);
        this.status = 2;
    }

    /**
     * 回收文件
     */
    public void recycle(RandomAccessFile raf, byte[] head) throws IOException {
        this.deleted = 0;
        editDelete(raf, head, (byte) 0);
        this.status = 1;
    }

    private void editDelete(RandomAccessFile raf, byte[] head, byte del) throws IOException {
        long delOffset = offset + head.length + fileId.getBytes(StandardCharsets.UTF_8).length;
        raf.seek(delOffset);
        raf.write(del);
    }

    /**
     * 将给定的DataNode对象的所有信息都转储到自身
     * @param dataNode 需要被复制的数据来源对象
     */
    public void loadByDataNode(DataNode dataNode) {
        if (Objects.isNull(dataNode))
            return;
        this.deleted = dataNode.getDeleted();
        this.type = dataNode.getType();
        this.fileId = dataNode.getFileId();
        this.dataName = dataNode.getDataName();
        this.offset = dataNode.getOffset();
        this.size = dataNode.getSize();
        this.mdOffset = dataNode.getMdOffset();
        this.status = 1;
    }

}
