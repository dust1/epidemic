package com.dust.epidemic.data.btree;

public class Node<K extends Comparable<K>, V> {

    /**
     * 给定检索关键字k、数组keys、数据长度len。
     * 从给定长度的集合中查询符合条件的k的下标，要求：
     *      1. 如果集合中存在k，则返回它的下标
     *      2. 如果集合中不存在k，则返回它应该插入的下标
     * @param k
     * @param keys
     * @param len
     * @return
     */
    public int findK(K k, K[] keys, int len) {
        int l = -1, r = len;
        int mid;
        while (l + 1 < r) {
            mid = (l + r) / 2;
            if (keys[mid].compareTo(k) == 0) {
                return mid;
            } else if (keys[mid].compareTo(k) < 0) {
                l = mid;
            } else {
                r = mid;
            }
        }
        return r;
    }

    public static void main(String[] args) {
        Node<Integer, String> node = new Node<>();
        Integer[] keys = {2, 4, 6, 10};
        System.out.println(node.findK(1, keys, keys.length));
        System.out.println(node.findK(3, keys, keys.length));
        System.out.println(node.findK(4, keys, keys.length));
        System.out.println(node.findK(19, keys, keys.length));
    }

}
