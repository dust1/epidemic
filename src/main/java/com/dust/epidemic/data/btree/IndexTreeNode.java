package com.dust.epidemic.data.btree;

/**
 * 索引节点
 */
public class IndexTreeNode <K extends Comparable<K>, V> extends AbstractTreeNode<K, V> {

    protected IndexTreeNode(int orderNum) {
        super(orderNum);
    }

    @Override
    public TreeNode<K, V> insert(K k, V v) {
        return null;
    }

    @Override
    public TreeNode<K, V> delete(K k) {
        return null;
    }

    @Override
    public V find(K k) {
        return null;
    }

    @Override
    protected TreeNode<K, V> splitMe() {
        return null;
    }

    /**
     * 子节点往上推送上来的新节点
     * @param newLeft 新节点所在的左子节点
     * @param newRight 新节点所在的右子节点
     * @param pushKey 新节点的Key
     */
    public TreeNode<K, V> pushNode(TreeNode<K, V> newLeft, TreeNode<K, V> newRight, K pushKey) {

    }
}
