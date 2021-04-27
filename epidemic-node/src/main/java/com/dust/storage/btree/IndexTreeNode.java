package com.dust.storage.btree;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

/**
 * 索引节点
 */
public class IndexTreeNode extends AbstractTreeNode {

    private TreeNode[] childs;

    protected IndexTreeNode(int orderNum) {
        super(orderNum);
        this.childs = new TreeNode[orderNum + 1];
    }

    @Override
    public TreeNode insert(String k) {
        int pos = searchKey(k);
        if (pos < size && k.compareTo(keys[pos]) == 0) {
            pos += 1;
        }

        return childs[pos].insert(k);
    }

    @Override
    public TreeNode delete(String k) {
        return null;
    }

    @Override
    public DataNode find(String k) {
        int pos = searchKey(k);
        if (pos < size && k.compareTo(keys[pos]) == 0) {
            pos += 1;
        }

        return childs[pos].find(k);
    }

    @Override
    protected TreeNode splitMe() {
        int mid = size / 2;
        //这个key要往上推送
        String pushKey = keys[mid];

        IndexTreeNode newRight = new IndexTreeNode(orderNum);
        //对于索引节点来说，这个关键字上移了
        newRight.setSize(size - mid - 1);
        newRight.setKeys(keys, mid + 1, size - mid - 1);
        newRight.setParent(parent);
        if (Objects.isNull(parent)) {
            IndexTreeNode p = new IndexTreeNode(orderNum);
            parent = p;
            newRight.setParent(p);
        }
        newRight.setChilds(childs, mid +  1, size - mid);
        setSize(mid);
        return parent.pushNode(this, newRight, pushKey);
    }

    private void setChilds(TreeNode[] source, int start, int len) {
        System.arraycopy(source, start, childs, 0, len);
        for (int i = 0; i < len; i++) {
            TreeNode child = childs[i];
            if (child instanceof AbstractTreeNode) {
                AbstractTreeNode node = (AbstractTreeNode) child;
                node.setParent(this);
            }
        }
    }

    /**
     * 子节点往上推送上来的新节点
     * @param newLeft 新节点所在的左子节点,这个是原有的叶子节点
     * @param newRight 新节点所在的右子节点
     * @param pushKey 新节点的Key
     */
    public TreeNode pushNode(TreeNode newLeft, TreeNode newRight, String pushKey) {
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
        return Objects.isNull(parent) ? this : null;
    }

    public void print() {
        Queue<AbstractTreeNode> queue = new LinkedList<>();
        queue.add(this);
        queue.add(null);
        StringBuilder sb = new StringBuilder();
        while (!queue.isEmpty()) {
            AbstractTreeNode node = queue.poll();
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
                    IndexTreeNode n = (IndexTreeNode) node;
                    for (int i = 0; i <= n.size; i++) {
                        queue.add((AbstractTreeNode) n.childs[i]);
                    }
                }
            }
        }
    }

    /**
     * 获取索引节点中首个左子节点
     * @return
     */
    public TreeNode getFirstChild() {
        if (size <= 0) {
            return null;
        }
        return childs[0];
    }

    public String toString() {
        return Arrays.toString(keys);
    }

}
