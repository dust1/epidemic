package com.dust.epidemic;

import com.dust.epidemic.data.btree.BTreeManager;
import com.dust.epidemic.data.btree.DataNode;

import java.util.Iterator;

public class MyBTreeTest {

    public static void main(String[] args) {
        BTreeManager manager = BTreeManager.create(8);
        int asc = (int) 'A';
        for (int i = 0; i < 26; i++) {
            String key = String.valueOf((char) (asc + i));
            manager.insert(key);
        }

        Iterator<DataNode> iterator = manager.iterator();
        while (iterator.hasNext()) {
            DataNode dataNode = iterator.next();
            System.out.println(dataNode.getKey());
        }
    }

}
