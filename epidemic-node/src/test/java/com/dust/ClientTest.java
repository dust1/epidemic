package com.dust;

import com.dust.fundation.EpidemicUtils;
import com.dust.grpc.EpidemicClient;
import com.dust.grpc.kademlia.StoreRequest;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannelBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.junit.Assert.*;

public class ClientTest {

    private EpidemicClient client;

    @Before
    public void before() {
        var channel = ManagedChannelBuilder
                .forAddress("localhost", 8080)
                .usePlaintext()
                .build();

        client = new EpidemicClient(channel);
    }

    @Test
    public void pingTest() {
        for (int i = 0; i < 22; i++) {
            String key = System.currentTimeMillis() + "i=" + i;
            String myNodeId = EpidemicUtils.getSHA1(key.getBytes(StandardCharsets.UTF_8));
            client.ping(myNodeId);
        }
    }

    @Test
    public void storeTest() {
        String updateFile = "/Users/kous/Desktop/rainy.zip";
        client.store(updateFile);
    }

    @Test
    public void readFileTest() {
        String filePath = "/Users/kous/Desktop/rainy.zip";
        String key = System.currentTimeMillis() + "";
        String fileId = EpidemicUtils.getSHA1(key.getBytes(StandardCharsets.UTF_8));
        try (var input = new RandomAccessFile(new File(filePath), "r")) {
            ByteBuffer buffer = ByteBuffer.allocate((int) input.length());
            final FileChannel channel = input.getChannel();
            channel.read(buffer);
            buffer.flip();
            ByteString data = ByteString.copyFrom(buffer);
            System.out.println(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String randomNodeId() {
        Random random = new Random();
        int i = random.nextInt(299292);
        String key = System.currentTimeMillis() + "i=" + i;
        return EpidemicUtils.getSHA1(key.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void findNodeTest() {
        String targetId = randomNodeId();
        String sourceId = randomNodeId();
        int sourcePort = 8080;

        int size = 0;
        var iter = client.findNode(targetId, sourceId, sourcePort);
        while (iter.hasNext()) {
            var resNode = iter.next();
            size += 1;
            System.out.println(resNode);
        }
        assertEquals(size, 20);
    }

}
