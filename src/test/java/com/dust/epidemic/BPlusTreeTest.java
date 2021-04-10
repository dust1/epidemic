package com.dust.epidemic;

import com.dust.epidemic.data.BPlusTree;

import java.util.Random;

public class BPlusTreeTest {

    public static void main(String[] args) {
        BPlusTree<Integer, Integer> tree = new BPlusTree<>(4);
        tree.insert(1, 1);
        System.out.println(tree.find(1));
        tree.insert(1, 2);
        System.out.println(tree.find(1));
    }

}
