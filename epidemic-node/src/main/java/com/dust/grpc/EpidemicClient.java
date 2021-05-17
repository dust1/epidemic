package com.dust.grpc;

import com.dust.fundation.EpidemicUtils;
import com.dust.grpc.kademlia.*;
import com.google.protobuf.ByteString;
import io.grpc.Channel;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * 节点客户端，用于与其他节点通信
 */
public class EpidemicClient {

    private KademliaServiceGrpc.KademliaServiceBlockingStub blockingStub;

    public EpidemicClient(Channel channel) {
        this.blockingStub = KademliaServiceGrpc.newBlockingStub(channel);
    }

    public void ping(String nodeId) {
        var info = NodeInfo.newBuilder().setNodeId(nodeId)
                .setPort(7000)
                .build();
        var req = PingRequest.newBuilder()
                .setNodeInfo(info)
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
            var nodeInfo = NodeInfo.newBuilder().setNodeId("sss")
                    .setPort(7000)
                    .build();
            var req = StoreRequest.newBuilder().setNodeInfo(nodeInfo)
                    .setData(ByteString.copyFrom(buffer))
                    .build();
            var res = blockingStub.store(req);
            System.out.println(res.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Iterator<FindNodeResponse> findNode(String targetId, String sourceId, int sourcePort) {
        var nodeInfo = createNodeInfo(sourceId, sourcePort);
        var req = FindRequest.newBuilder().setNodeInfo(nodeInfo)
                .setTargetId(targetId)
                .build();
        return blockingStub.findNode(req);
    }

    public Iterator<FindValueResponse> findValue(String targetId, String sourceId, int sourcePort) {
        var nodeInfo = createNodeInfo(sourceId, sourcePort);
        var req = FindRequest.newBuilder().setNodeInfo(nodeInfo)
                .setTargetId(targetId)
                .build();
        return blockingStub.findValue(req);
    }

    private NodeInfo createNodeInfo(String nodeId, int port) {
        return NodeInfo.newBuilder().setNodeId(nodeId)
                .setPort(port)
                .build();
    }

}
