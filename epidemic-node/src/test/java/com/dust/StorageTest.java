package com.dust;

import com.dust.router.kademlia.NodeTriadRouterNode;
import org.junit.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static org.junit.Assert.*;

/**
 * 存储测试类
 */
public class StorageTest {

    private String path = "/Users/kous/myProjects/javaproject/epidemic/temp/node.cache";

    /**
     * 持有读写锁的对象存在的情况下，测试另一个读对象能够使用
     */
    @Test
    public void writingAndReadTest() throws IOException {
        RandomAccessFile writing = new RandomAccessFile(path, "rw");
        writing.seek(0);
        final FileChannel channel = writing.getChannel();

        RandomAccessFile read = new RandomAccessFile(path, "r");
        read.seek(0);
        var node = NodeTriadRouterNode.fromFile(read);
        assertNotNull(node);
        System.out.println("Node info key=" + node.getKey() + ", host=" + node.getHost());
        assertEquals(writing.getFilePointer(), 0);
    }

    @Test
    public void readTest() throws IOException {
        RandomAccessFile writing = new RandomAccessFile(path, "r");
        writing.seek(0);
        final FileChannel channel = writing.getChannel();
        ByteBuffer data = ByteBuffer.allocate(40);
        int len = channel.read(data);
        assertEquals(len, 40);
    }

    @Test
    public void writeTest() throws IOException {
        RandomAccessFile writing = new RandomAccessFile(path, "rw");
        writing.seek(writing.length());
        final FileChannel channel = writing.getChannel();
        ByteBuffer data = ByteBuffer.allocate(40);
        for (int i = 0; i < data.position(); i++) {
            data.put((byte) i);
        }
        int len = channel.write(data);
        assertEquals(len, 40);
    }

}
