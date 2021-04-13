package com.dust.epidemic;

import com.dust.epidemic.data.btree.BTreeManager;

public class MyBTreeTest {

    public static void main(String[] args) {
        BTreeManager<Integer, String> manager = BTreeManager.create(3);
        manager.insert(1, "A");
        manager.insert(2, "B");
        manager.insert(3, "C");

        manager.insert(4, "D");
        manager.insert(5, "E");
        manager.insert(6, "F");

        manager.insert(7, "G");
        manager.insert(8, "H");
        manager.insert(9, "I");

        manager.insert(10, "J");

        manager.print();
    }

}
