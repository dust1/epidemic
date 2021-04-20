package com.dust.epidemic.data.btree;

import com.dust.epidemic.foundation.buffer.ReusableBuffer;
import com.dust.epidemic.fs.*;

import java.util.*;

/**
 * B树目录中数据的存储节点，用于维护数据本身以及相关描述
 *
 * 数据并不会保存在这个对象中，这个对象持有对本地数据的引用
 * 对接着fs与目录树
 *
 * 就是文件的虚拟对象，同时承担着文件读写入口的工作
 *
 * 每个DataNode只会对应一个文件
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
    private String virtualKey;

    /**
     * 文件名称
     * 这个文件名称表示存入这个DataNode的文件名称，而不是fs模块的文件名称
     */
    private String fileName;

    /**
     * 文件虚拟对象的状态
     */
    private int working;

    /**
     * 创建时间
     */
    private int createTime;

    /**
     * 修改时间
     */
    private int updateTime;

    /**
     * 这个文件对应的磁盘文件id与对象id
     */
    private Map<String, List<Long>> fileIdWithObjIdMap;

    /**
     * 存储对象
     */
    private StorageLayout storageLayout;

    public DataNode(String key, String fileName) {
        this.fileName = fileName;
        this.virtualKey = key;
        this.working = 0;

        int timestamp = (int) (System.currentTimeMillis() / 1000);
        this.createTime = timestamp;
        this.updateTime = timestamp;
    }

    /**
     * 给存储节点添加存储系统对象
     * @param storageLayout 添加的存储系统对象
     */
    public void init(StorageLayout storageLayout) {
        assert Objects.nonNull(storageLayout);
        this.storageLayout = storageLayout;
        this.fileIdWithObjIdMap = new HashMap<>();
        working = 1;
    }

    /**
     * 写入数据
     * @return 表示实际写入的数据长度，-1表示数据写入失败。
     */
    public int write(ReusableBuffer data) {
        checkStatus();
        //TODO 写入数据都是对DataNode中配置的文件的追加，每个DataNode只会对应一个文件
        return -1;
    }

    /**
     * 通过文件id与对象id获取这个对象的数据
     * @param objId 对象id，这里的ID不等于存储系统中的对象id，这个id是由fileId和objNo组合而成
     *              为了保证数据的安全性，并不能将fileId直接对外公布，因为fileId不一定都是这个DataNode的数据
     *              所以通过DataNode都只能操作这个DataNode所属的所有object。
     */
    public ObjectInformation read(String objId) {
        checkStatus();
        //TODO
        return null;
    }

    /**
     * 读取这个节点所有数据
     */
    public List<ObjectInformation> readAll() {
        checkStatus();

        //TODO
        return Collections.emptyList();
    }

    /**
     * 获取这个节点所有的objectId
     */
    public List<String> getAllObjectId() {

        //TODO
        return Collections.emptyList();
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
     */
    public boolean destroy() {
        if (working == INIT) {
            //这个节点并没有关联到物理磁盘，直接返回，等待GC将其回收
            return true;
        }
        working = DESTROY;


        //TODO 释放磁盘空间
        return false;
    }

}
