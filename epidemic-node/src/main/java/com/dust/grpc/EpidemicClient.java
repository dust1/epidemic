package com.dust.grpc;

import com.dust.fundation.EpidemicUtils;
import com.dust.grpc.kademlia.KademliaServiceGrpc;
import com.dust.grpc.kademlia.PingRequest;
import com.dust.grpc.kademlia.StoreRequest;
import com.google.protobuf.ByteString;
import io.grpc.Channel;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * 节点客户端，用于与其他节点通信
 */
public class EpidemicClient {

    private KademliaServiceGrpc.KademliaServiceBlockingStub blockingStub;

    public EpidemicClient(Channel channel) {
        this.blockingStub = KademliaServiceGrpc.newBlockingStub(channel);
    }

    public void ping(String nodeId) {
        var req = PingRequest.newBuilder().setNodeId(nodeId)
                .setPort(7000)
                .setTimestamp((int) (System.currentTimeMillis() / 1000))
                .build();
        var res = blockingStub.ping(req);
        System.out.println(res.toString());
    }

    public void store(String filePath) {
        String key = System.currentTimeMillis() + "";
        String fileId = EpidemicUtils.getSHA1(key.getBytes(StandardCharsets.UTF_8));
        try (var input = new RandomAccessFile(new File(filePath), "r")) {
            ByteBuffer buffer = ByteBuffer.allocate((int) input.length());
            final FileChannel channel = input.getChannel();
            channel.read(buffer);
            buffer.flip();
            var req = StoreRequest.newBuilder().setKey(fileId)
                    .setData(ByteString.copyFrom(buffer))
                    .build();
            var res = blockingStub.store(req);
            System.out.println(res.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
