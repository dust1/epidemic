package com.dust;

import com.dust.storage.btree.BTreeManager;
import com.dust.storage.btree.DataNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class BTreeTest {

    private BTreeManager manager;

    @Before
    public void before() {
        manager = BTreeManager.create();
    }

    @Test
    public void insertWithUpdateTest() {
        String key1 = "a";
        DataNode dataNode = manager.insert(key1);
//        dataNode.setFileId("test");

        DataNode dataNode1 = manager.insert(key1);
        assertNotEquals(dataNode1, dataNode);
    }

}
