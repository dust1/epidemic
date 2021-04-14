package com.dust.epidemic.data;

import com.dust.epidemic.data.btree.BTreeManager;
import com.dust.epidemic.data.btree.DataNode;
import io.vertx.ext.web.impl.LRUCache;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
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
     * 本地存储路径
     */
    private String dirPath;

    /**
     * 文件存储目录文件对象
     * {@link java.io.File}并不会将文件读取到内存中，真正会占用文件引用的只有文件IO。
     * 因此可以将这个对象一致持有
     */
    private File dirFile;

    public DataManager() {
        this(11, 50, null);
    }

    /**
     *
     * @param orderNum
     * @param maxSize LRU换粗队列长度，最好设置成大于N/2（N集群中节点数量）
     */
    public DataManager(int orderNum, int maxSize, String dirPath) {
        this.cache = new LRUCache<>(maxSize);
        this.bTreeManager = BTreeManager.create(orderNum);
        this.dirPath = dirPath;
        this.dirFile = new File(dirPath);
    }

    /**
     * 要对数据进行写入，从目录中获取这个文件的虚拟对象
     * 并让这个对象加载路径以达到能够写入文件的状态
     */
    public DataNode write(String key, long len) {
        long usableSpace = getUsableSpace();
        if (usableSpace <= len) {
            return null;
        }
        DataNode dataNode = bTreeManager.insert(key);
        dataNode.loadDir(dirPath, len);
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
     * 查询剩余空间
     */
    public long getUsableSpace() {
        return dirFile.getUsableSpace();
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


}
