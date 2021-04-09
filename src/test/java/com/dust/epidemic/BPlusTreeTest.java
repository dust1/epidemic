package com.dust.epidemic;

import com.dust.epidemic.data.BPlusTree;

import java.util.Random;

public class BPlusTreeTest {

    public static void main(String[] args) {
        BPlusTree<Integer, Integer> tree = new BPlusTree<>(4);
        for (int i = 0; i < 100000; i++) {
            long start = System.currentTimeMillis();
            tree.insert(i, i);
            long end = System.currentTimeMillis();
//            System.out.println("插入个数" + i + ";所需时间：" + ((end - start)));
        }

        Random random = new Random();
        for (int i = 0; i < 100000; i++) {
            int index = random.nextInt(100000);
            long start = System.currentTimeMillis();
            int result = tree.find(index);
            long end = System.currentTimeMillis();
            System.out.println("随机查询" + index + ";所需时间：" + (end - start) + ";查询结果:" + result);
        }
    }

}
