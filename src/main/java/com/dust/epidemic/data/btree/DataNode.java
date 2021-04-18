package com.dust.epidemic.data.btree;

import com.dust.epidemic.core.EpidemicConfig;
import com.dust.epidemic.foundation.buffer.ReusableBuffer;
import com.dust.epidemic.fs.MetadataCache;
import com.dust.epidemic.fs.StorageLayout;
import com.dust.epidemic.fs.StorageLayoutFactory;

import java.util.List;
import java.util.Map;
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
     * 虚拟id，这个只是存入文件的id
     * 注意这有两个文件id，一个是对于用户与目录树来说的文件id，这个id用于叶子节点检索
     * 还有一个文件id表示的是.data文件的id
     */
    private String virtualKey;

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
    private Map<String, List<String>> fileIdWithObjIdMap;

    /**
     * 存储对象
     */
    private StorageLayout storageLayout;

    /**
     * 写入数据
     */
    public void write(ReusableBuffer data) {
        if (Objects.isNull(storageLayout)) {
            throw new NullPointerException("the storagelayout was not init in DataNode.");
        }

    }

    /**
     * 初始化存储对象
     * @param config
     * @param cache
     */
    public void init(EpidemicConfig config, MetadataCache cache) {
        if (Objects.isNull(storageLayout)) {
            this.storageLayout = StorageLayoutFactory.createFile(config, cache);
        }
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
