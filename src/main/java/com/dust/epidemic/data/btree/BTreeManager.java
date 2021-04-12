package com.dust.epidemic.data.btree;

import java.util.Objects;

/**
 * B+树管理器
 */
public class BTreeManager<K extends Comparable<K>, V> {

    /**
     * 默认阶数
     */
    private static final int DEFAULT_ORDER_NUM = 11;

    private TreeNode<K, V> root;

    private int orderNum;

    public static <K extends Comparable<K>, V> BTreeManager<K, V> create() {
        return create(DEFAULT_ORDER_NUM);
    }

    public static <K extends Comparable<K>, V> BTreeManager<K, V> create(int orderNum) {
        BTreeManager<K, V> manager = new BTreeManager<>(orderNum);
        manager.init();
        return manager;
    }


    private BTreeManager(int orderNum) {
        this.orderNum = orderNum;
    }

    /**
     * 初始化方法
     */
    private void init() {
        this.root = new LeafTreeNode<>(orderNum);
    }

    /**
     * 添加元素
     */
    public void insert(K key, V value) {
        root = root.insert(key, value);
    }

    /**
     * 查询元素
     */
    public V find(K key) {
        return root.find(key);
    }

    /**
     * 删除元素
     */
    public V delete(K key) {
        V v = root.find(key);
        if (Objects.isNull(v)) {
            return null;
        }
        root = root.delete(key);
        return v;
    }

    /**
     * 将元素序列化
     * @return
     */
    public byte[] toBytes() {
        //TODO
        return null;
    }

    /**
     * 根据序列化后的元素重建树
     * @param b
     * @return
     */
    public boolean byBytesWithCreate(byte[] b) {
        //TODO
        return false;
    }

    /**
     * 根据序列化后的元素将结果合并到当前目录
     * @param b
     * @return
     */
    public boolean byBytesWithMerge(byte[] b) {
        //TODO
        return false;
    }

}
