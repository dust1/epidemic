package com.dust.epidemic.data;

import com.dust.epidemic.core.EpidemicConfig;
import com.dust.epidemic.data.btree.BTreeManager;
import com.dust.epidemic.data.btree.DataNode;
import com.dust.epidemic.foundation.LRUCache;
import com.dust.epidemic.fs.MetadataCache;
import com.dust.epidemic.fs.StorageLayout;
import com.dust.epidemic.fs.StorageLayoutFactory;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.util.Objects;


/**
 * 节点中对所有存储信息的管理表
 * 数据保存在磁盘中，B+树只是作为一棵索引结构
 *
 * 传输的是数据流
 */
@Getter
@Setter
public class DataManager {

    private int orderNum;

    private int maxSize;

    private EpidemicConfig config;

    /**
     * LRU缓存实现
     * 用来记录已经接收的请求，避免循环
     */
    private LRUCache<String, Boolean> cache;

    /**
     * 存储目录树
     */
    private BTreeManager bTreeManager;

    /**
     * 文件系统对象
     */
    private StorageLayout storageLayout;

    /**
     * 只能通过构造极构建
     */
    public static DataManagerBuilder builder() {
        return new DataManagerBuilder();
    }

    private DataManager(DataManagerBuilder builder) throws IOException {
        this.orderNum = builder.orderNum;
        this.maxSize = builder.maxSize;
        this.config = builder.config;
        this.cache = new LRUCache<>(maxSize);
        this.bTreeManager = BTreeManager.create(orderNum);

        init();
    }

    /**
     * 初始化文件系统
     */
    private void init() throws IOException {
        storageLayout = StorageLayoutFactory.createFile(config, new MetadataCache());
    }

    /**
     * 在写入前获取DataNode，其实就是根据文件id先创建对应的DataNode对象
     */
    public DataNode getDataNodeBeforeWrite(String key) {
        DataNode dataNode = bTreeManager.insert(key);
        dataNode.init(storageLayout);
        return dataNode;
    }

    /**
     * 根据关键字释放文件
     */
    public boolean destroy(String key) {
        DataNode dataNode = bTreeManager.find(key);
        if (Objects.isNull(dataNode)) {
            return false;
        }

        //先释放物理空间，再在内存中修改
        //否则当在内存中表现为文件已删除但是依旧无法得到物理空间
        //因为此时物理删除部分还在工作
        boolean result = dataNode.destroy();
        bTreeManager.delete(key);
        return result;
    }

    /**
     * 根据DataNode释放文件
     */
    public boolean destroy(DataNode dataNode) {
        boolean result = dataNode.destroy();
        String key = dataNode.getKey();
        bTreeManager.delete(key);
        return result;
    }

    /**
     * 检查是否已经接受过这个数据包
     * @param sign 数据包的唯一标识符
     */
    public boolean check(String sign) {
        if (cache.containsKey(sign)) {
            return false;
        }
        cache.put(sign, true);
        return true;
    }

    /**
     * 将另一个节点的数据跟自身数据合并
     */
    public void merge() {

    }

    /**
     * 数据管理构造器
     */
    static class DataManagerBuilder {
        /**
         * B+数的阶数
         */
        private int orderNum;

        /**
         * 记录每个请求的LRU队列数量
         */
        private int maxSize;

        /**
         * 系统配置文件
         */
        private EpidemicConfig config;

        public DataManagerBuilder() {
            this.orderNum = 5;
            this.maxSize = 100;
            this.config = null;
        }

        public DataManagerBuilder orderNum(int orderNum) {
            this.orderNum = orderNum;
            return this;
        }

        public DataManagerBuilder maxSize(int maxSize) {
            this.maxSize = maxSize;
            return this;
        }

        public DataManagerBuilder config(EpidemicConfig config) {
            this.config = config;
            return this;
        }

        public DataManager build() throws IOException {
            return new DataManager(this);
        }

    }

}
