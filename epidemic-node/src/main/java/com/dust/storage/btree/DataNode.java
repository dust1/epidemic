package com.dust.storage.btree;

import com.dust.storage.StorageLayout;
import com.dust.storage.buffer.ReusableBuffer;

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
public class DataNode {

    //初始化完成，但还无法开始工作。因为还没有载入存储系统
    private static final int INIT       = 0;

    //已经载入存储系统，可以开始工作
    private static final int WORKING    = 1;

    //要被释放，无法继续提供服务
    private static final int DESTROY    = 2;

    /**
     * 虚拟id，这个只是存入文件的id
     * 注意这有两个文件id，一个是对于用户与目录树来说的文件id，这个id用于叶子节点检索
     * 还有一个文件id表示的是.data文件的id
     */
    private String key;

    /**
     * 文件虚拟对象的状态
     */
    private int working;

    /**
     * 存储对象
     */
    private StorageLayout storageLayout;


    public DataNode(String key) {
        this.key = key;
        this.working = 0;
    }

    /**
     * 给存储节点添加存储系统对象
     * @param storageLayout 添加的存储系统对象
     */
    public void init(StorageLayout storageLayout) {
        assert Objects.nonNull(storageLayout);
        this.storageLayout = storageLayout;
        working = 1;
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
     * 读取这个DataNode对应的文件块数据
     * @return 读取到的文件块数据，如果返回对象为null则表示文件不存在
     */
    public ReusableBuffer read() {
//        checkStatus();
//
//        FileAndObj fileAndObj = decode(objectId);
//        if (Objects.isNull(fileAndObj)) {
//            return ObjectInformation.fail("传入的objectId格式错误");
//        }
//
//        try {
//            FileMetadata md = storageLayout.getFileMetadata(fileAndObj.getFileId());
//            if (Objects.isNull(md)) {
//                return ObjectInformation.fail("不存在的.data文件");
//            }
//
//            long version = md.getLatestObjectVersionByObj(fileAndObj.objNo);
//            if (version == -1) {
//                return ObjectInformation.fail("不存在的对象版本");
//            }
//
//            return storageLayout.readObject(fileAndObj.fileId, md, fileAndObj.objNo, 0, -1, version);
//        } catch (IOException e) {
//            //TODO 读取失败记录日志
//            return ObjectInformation.fail(e.getMessage());
//        }
        return null;
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
