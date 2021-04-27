package com.dust.storage.btree;

/**
 * 树的抽象类，用于抽象叶子节点与索引节点的公共方法与变量
 */
public abstract class AbstractTreeNode implements TreeNode {

    /**
     * 节点保存的关键字
     */
    protected String[] keys;

    /**
     * 节点的父节点
     */
    protected IndexTreeNode parent;

    /**
     * 树的阶数 - 每个节点最大的索引数量
     */
    protected int orderNum;

    /**
     * 节点中的索引数量
     */
    protected int size;


    protected AbstractTreeNode(int orderNum) {
        this.orderNum = orderNum;
        this.size = 0;
        this.keys = new String[orderNum];
        this.parent = null;
    }

    /**
     * 从关键字集合中检索关键字，如果存在则返回关键字所在的下标，如果不存在则判断插入关键字时它应该在的下标
     */
    protected int searchKey(String k) {
        int l = -1, r = size;
        int mid;
        while (l + 1 < r) {
            mid = (l + r) / 2;
            String key = keys[mid];
            if (key.compareTo(k) == 0) {
                return mid;
            } else if (key.compareTo(k) < 0) {
                l = mid;
            } else {
                r = mid;
            }
        }
        return r;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setParent(IndexTreeNode parent) {
        this.parent = parent;
    }

    public void setKeys(String[] source, int start, int len) {
        System.arraycopy(source, start, keys, 0, len);
    }

    /**
     * 节点拆分
     */
    abstract protected TreeNode splitMe();

}
