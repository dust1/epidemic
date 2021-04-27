package com.dust.storage.btree;

import java.util.Iterator;
import java.util.Objects;

/**
 * B+树管理器
 */
public class BTreeManager {

    /**
     * 默认阶数
     */
    private static final int DEFAULT_ORDER_NUM = 11;

    /**
     * 根节点
     */
    private TreeNode root;

    private int orderNum;

    public static BTreeManager create() {
        return create(DEFAULT_ORDER_NUM);
    }

    public static BTreeManager create(int orderNum) {
        return new BTreeManager(orderNum);
    }


    private BTreeManager(int orderNum) {
        this.orderNum = orderNum;
        this.root = new LeafTreeNode(orderNum);
    }

    /**
     * 添加元素
     * @return 返回数据节点信息，后续的文件写入都通过这个对象写入，而不是通过目录
     */
    public DataNode insert(String key, String fileName) {
        TreeNode result = root.insert(key, fileName);
        if (Objects.nonNull(result)) {
            root = result;
        }
        return find(key);
    }

    /**
     * 获取树的最小元素
     */
    private LeafTreeNode findMin() {
        if (Objects.isNull(root)) {
            return null;
        }

        TreeNode head = root;
        while (Objects.nonNull(head) && head instanceof IndexTreeNode) {
            IndexTreeNode index = (IndexTreeNode) head;
            head = index.getFirstChild();
        }
        if (Objects.isNull(head) || !(head instanceof LeafTreeNode)) {
            return null;
        }
        return (LeafTreeNode) head;
    }

    /**
     * 查询元素
     */
    public DataNode find(String key) {
        return root.find(key);
    }

    /**
     * 删除元素
     */
    public DataNode delete(String key) {
        DataNode v = root.find(key);
        if (Objects.isNull(v)) {
            return null;
        }
        root = root.delete(key);
        return v;
    }

    /**
     * 获取目录树的顺序迭代器
     * @return
     */
    public Iterator<DataNode> iterator() {
        return new DataIterator(findMin());
    }

}
