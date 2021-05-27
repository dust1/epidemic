package com.dust;

import com.dust.router.kademlia.NodeTriadRouterNode;
import com.dust.storage.FileStorageLayout;
import com.dust.storage.StorageLayout;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * 存储测试类
 */
public class StorageTest {

    private String configPath = "/Users/kous/Desktop/setting.conf";

    private StorageLayout storageLayout;

    @Before
    public void before() throws Exception {
        Properties properties = new Properties();
        try (var input = new FileReader(configPath)) {
            properties.load(input);
        }
        var config = new NodeConfig(properties);
        storageLayout = FileStorageLayout.create(config, "localNode");
        storageLayout.before();
    }

    @Test
    public void storeTest() throws IOException {
        String filePath = "/Users/kous/Downloads/yellow.zip";
        String fileId = "6014554976340352799ece63d50b5bc73a6dc05e";
        String savePath = "/Users/kous/myProjects/javaproject/epidemic/temp/storage";

        writeStore(filePath, fileId);
        String fileName = "6eb668da5c012fc3d7d5d4a957cfdd7752d52655.md";
        try (var raf = new RandomAccessFile(new File(savePath, fileName), "r")) {
            long offset = 4 + fileId.getBytes(StandardCharsets.UTF_8).length;
            raf.seek(offset);
            var del = raf.readByte();
            assertEquals(del, 0);
        }

        storageLayout.delete(fileId);
        try (var raf = new RandomAccessFile(new File(savePath, fileName), "r")) {
            long offset = 4 + fileId.getBytes(StandardCharsets.UTF_8).length;
            raf.seek(offset);
            var del = raf.readByte();
            assertEquals(del, 1);
        }
    }

    @Test
    public void readTest() throws IOException {
        String filePath = "/Users/kous/Downloads/yellow.zip";
        String savePath = "/Users/kous/Desktop/yellow.zip";
        String fileId = "yellow01";
        String fileId2 = "yellow02";
        var data = storageLayout.find(fileId);
        assertEquals(data.position(), 0);
        writeStore(filePath, fileId);
        data = storageLayout.find(fileId);
        assertEquals(data.position(), new File(filePath).length());
    }

    private void readFile(String savePath, String fileId) throws IOException {
        ByteBuffer buffer = storageLayout.find(fileId);
        try (var write = new RandomAccessFile(savePath, "rw");
             var channel = write.getChannel()) {
            channel.write(buffer);
        }
    }

    private void writeStore(String filePath, String fileId) throws IOException {
        ByteBuffer data;
        try (var read = new RandomAccessFile(filePath, "r");
             var channel = read.getChannel()) {
            data = ByteBuffer.allocate((int) read.length());
            channel.read(data);
        }
        data.flip();
        storageLayout.store(data, fileId);
    }

}
