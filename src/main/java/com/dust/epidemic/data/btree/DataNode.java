package com.dust.epidemic.data.btree;

import com.dust.epidemic.fs.FSNode;

import java.util.Objects;

/**
 * B树目录中数据的存储节点，用于维护数据本身以及相关描述
 *
 * 数据并不会保存在这个对象中，这个对象持有对本地数据的引用
 * 对接着fs与目录树
 *
 * 就是文件的虚拟对象，同时承担着文件读写入口的工作
 */
public class DataNode {

    /**
     * 虚拟关键字
     * 文件对应的文件目录关键字
     * 这个关键字用于查找对应的文件虚拟对象
     */
    private String virtualKey;

    /**
     * 物理关键字
     * 这个关键字是DataNode要进行写入操作的时候由fs模块给出的本机全局唯一的UUID
     * 通过这个关键字来对文件进行物理上的操作
     */
    private String physicalKey;

    /**
     * 文件系统对象
     */
    private FSNode fsNode;

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

    private String dirPath;

    /**
     * 加载文件路径
     * @param dirPath
     */
    public void loadDir(String dirPath, long len) {
        this.fsNode = FSNode.create(dirPath);
        if (Objects.isNull(fsNode)) {
            throw new NullPointerException("磁盘空间不足");
        }
        this.physicalKey = fsNode.init(virtualKey, len);
    }

    /**
     * 释放文件空间
     * @return
     */
    public boolean destroy() {
        if (working == 0) {
            //这个节点并没有关联到物理磁盘，直接返回，等待GC将其回收
            return true;
        }

        //TODO 释放磁盘空间
        return false;
    }

    public DataNode(String key) {
        this.virtualKey = key;
        this.working = 0;

        int timestamp = (int) (System.currentTimeMillis() / 1000);
        this.createTime = timestamp;
        this.updateTime = timestamp;
    }

    public String getKey() {
        return virtualKey;
    }

}
