package com.dust;

import com.dust.router.kademlia.NodeTriadRouterNode;
import com.dust.storage.FileStorageLayout;
import com.dust.storage.StorageLayout;
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
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
        String savePath = "/Users/kous/Desktop/yellow.zip";
        String fileId = "yellow01";
        String fileId2 = "yellow02";

        writeStore(filePath, fileId);
        readFile(savePath, fileId);
        writeStore(filePath, fileId);
        writeStore(filePath, fileId2);
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
