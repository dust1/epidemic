package com.dust.epidemic.data.btree;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

/**
 * 索引节点
 */
public class IndexTreeNode <K extends Comparable<K>, V> extends AbstractTreeNode<K, V> {

    private TreeNode<K, V>[] childs;

    protected IndexTreeNode(int orderNum) {
        super(orderNum);
        this.childs = new TreeNode[orderNum + 1];
    }

    @Override
    public TreeNode<K, V> insert(K k, V v) {
        int pos = searchKey(k);
        if (pos < size && k.compareTo((K) keys[pos]) == 0) {
            pos += 1;
        }

        return childs[pos].insert(k, v);
    }

    @Override
    public TreeNode<K, V> delete(K k) {
        return null;
    }

    @Override
    public V find(K k) {
        int pos = searchKey(k);
        if (pos < size && k.compareTo((K) keys[pos]) == 0) {
            pos += 1;
        }

        return childs[pos].find(k);
    }

    @Override
    protected TreeNode<K, V> splitMe() {
        int mid = size / 2;
        //这个key要往上推送
        K pushKey = (K) keys[mid];

        IndexTreeNode<K, V> newRight = new IndexTreeNode<>(orderNum);
        //对于索引节点来说，这个关键字上移了
        newRight.setSize(size - mid - 1);
        newRight.setKeys(keys, mid + 1, size - mid - 1);
        newRight.setParent(parent);
        boolean newParent = false;
        if (Objects.isNull(parent)) {
            newParent = true;
            IndexTreeNode<K, V> p = new IndexTreeNode<>(orderNum);
            parent = p;
            newRight.setParent(p);
        }

        newRight.setChilds(childs, mid +  1, size - mid);
        setSize(mid);
        TreeNode<K, V> result = parent.pushNode(this, newRight, pushKey);
        return newParent ? parent : result;
    }

    private void setChilds(TreeNode<K, V>[] source, int start, int len) {
        System.arraycopy(source, start, childs, 0, len);
    }

    /**
     * 子节点往上推送上来的新节点
     * @param newLeft 新节点所在的左子节点,这个是原有的叶子节点
     * @param newRight 新节点所在的右子节点
     * @param pushKey 新节点的Key
     */
    public TreeNode<K, V> pushNode(TreeNode<K, V> newLeft, TreeNode<K, V> newRight, K pushKey) {
        int pos = searchKey(pushKey);
        System.arraycopy(keys, pos, keys, pos + 1, size - pos);
        keys[pos] = pushKey;

        childs[pos] = newLeft;
        System.arraycopy(childs, pos + 1, childs, pos + 2,  size - pos);
        childs[pos + 1] = newRight;

        size += 1;
        if (size >= orderNum) {
            return splitMe();
        }
        return null;
    }

    public void print() {
        Queue<AbstractTreeNode<K, V>> queue = new LinkedList<>();
        queue.add(this);
        queue.add(null);
        StringBuilder sb = new StringBuilder();
        while (!queue.isEmpty()) {
            AbstractTreeNode<K, V> node = queue.poll();
            if (Objects.isNull(node)) {
                System.out.println(sb.toString());
                sb.delete(0, sb.length());
                if (queue.isEmpty()) {
                    break;
                }
                queue.add(null);
            } else {
                sb.append("[");
                for (int i = 0; i < node.size; i++) {
                    sb.append(node.keys[i]).append(" ");
                }
                sb.append("]");
                if (node instanceof IndexTreeNode) {
                    IndexTreeNode<K, V> n = (IndexTreeNode<K, V>) node;
                    for (int i = 0; i <= n.size; i++) {
                        queue.add((AbstractTreeNode<K, V>) n.childs[i]);
                    }
                }
            }

        }
    }

}
