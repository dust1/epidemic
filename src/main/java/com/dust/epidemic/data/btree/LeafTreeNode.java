package com.dust.epidemic.data.btree;

import java.util.Objects;

/**
 * 叶子节点
 */
public class LeafTreeNode extends AbstractTreeNode {

    /**
     * 叶子结点的两个兄弟节点
     */
    private LeafTreeNode prev;
    private LeafTreeNode next;

    /**
     * 叶子节点保存的数据内容
     */
    private DataNode[] dataNodes;

    public LeafTreeNode(int orderNum) {
        super(orderNum);
        this.prev = null;
        this.next = null;
        this.dataNodes = new DataNode[orderNum];
    }

    @Override
    protected TreeNode splitMe() {
        int mid = size / 2;
        String media = keys[mid];

        /*
         * 拆分的新节点，作为右半部分
         */
        LeafTreeNode tempNode = new LeafTreeNode(orderNum);
        tempNode.setSize(size - mid);
        tempNode.setParent(parent);
        if (Objects.isNull(parent)) {
            //如果父节点为空，则创建一个空的父节点并指向它
            IndexTreeNode p = new IndexTreeNode(orderNum);
            parent = p;
            tempNode.setParent(p);
        }

        tempNode.setKeys(keys, mid, size - mid);
        tempNode.setDataNodes(dataNodes, mid, size - mid);

        //将自身作为拆分的左半部分,值并不是删除了，只是交给其他节点，因此集合中的数据可以不删除，
        //但是要维护好边界，否则就访问到其他节点的元素了
        setSize(mid);

        if (Objects.nonNull(this.next)) {
            this.next.setPrev(tempNode);
        }
        tempNode.setNext(this.next);

        this.next = tempNode;
        tempNode.setPrev(this);

        //拆分成功后，将新的节点插入父节点（索引节点）
        return parent.pushNode(this, tempNode, media);
    }

    @Override
    public TreeNode insert(String k) {
        int pos = searchKey(k);
        System.arraycopy(keys, pos, keys, pos + 1, size - pos);
        System.arraycopy(dataNodes, pos, dataNodes, pos + 1, size - pos);
        keys[pos] = k;
        dataNodes[pos] = new DataNode(k);
        size += 1;

        if (size >= orderNum) {
            return splitMe();
        }
        //如果不用拆分直接返回null
        return null;
    }

    @Override
    public TreeNode delete(String k) {
        return null;
    }

    @Override
    public DataNode find(String k) {
        int pos = searchKey(k);
        if (pos < size && k.compareTo(keys[pos]) == 0) {
            return dataNodes[pos];
        }
        return null;
    }

    public void setDataNodes(DataNode[] source, int start, int len) {
        System.arraycopy(source, start, dataNodes, 0, len);
    }

    public void setPrev(LeafTreeNode prev) {
        this.prev = prev;
    }

    public void setNext(LeafTreeNode next) {
        this.next = next;
    }

    public DataNode[] getDataNodes() {
        return dataNodes;
    }

    public LeafTreeNode getNext() {
        return next;
    }
}
