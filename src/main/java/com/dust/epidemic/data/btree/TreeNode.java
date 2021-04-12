package com.dust.epidemic.data.btree;

/**
 * 树节点接口
 */
public interface TreeNode<K extends Comparable<K>, V> {

    /**
     * 添加节点
     * @return 返回对象主要是保证在插入后返回root节点
     */
    TreeNode<K, V> insert(K k, V v);

    /**
     * 删除节点
     * @return 理由同上
     */
    TreeNode<K, V> delete(K k);

    /**
     * 查询节点
     */
    V find(K k);

}
