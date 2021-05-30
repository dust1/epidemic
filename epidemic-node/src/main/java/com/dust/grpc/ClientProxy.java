package com.dust.grpc;

import com.dust.fundation.EpidemicUtils;
import com.dust.grpc.kademlia.*;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 客户端代理
 * 封装对其他节点的通信
 */
public class ClientProxy {

    private final NodeInfo nodeInfo;

    public static ClientProxy create(String localNodeId, int localPort) {
        return new ClientProxy(localNodeId, localPort);
    }

    private ClientProxy(String localNodeId, int localPort) {
        this.nodeInfo = NodeInfo.newBuilder()
                .setPort(localPort)
                .setNodeId(localNodeId)
                .build();
    }

    public PingResponse ping(String host, int port) {
        var client = buildBlockClient(host, port);
        var req = PingRequest.newBuilder().setNodeInfo(nodeInfo)
                .setTimestamp(EpidemicUtils.time())
                .build();
        return client.ping(req);
    }

    public StoreResponse store(String host, int port, ByteBuffer data, String fileId) {
        var client = buildBlockClient(host, port);
        var req = StoreRequest.newBuilder()
                .setNodeInfo(nodeInfo)
                .setData(ByteString.copyFrom(data))
                .setFileId(fileId)
                .build();
        return client.store(req);
    }

    public List<FindNodeResponse> findNode(String host, int port, String targetId) {
        var client = buildBlockClient(host, port);
        var req = buildFindRequest(targetId);
        var iter = client.findNode(req);
        return iterToList(iter);
    }

    public List<FindValueResponse> findValue(String host, int port, String fileId) {
        var client = buildBlockClient(host, port);
        var req = buildFindRequest(fileId);
        var iter = client.findValue(req);
        return iterToList(iter);
    }

    /*
    将迭代器转化为集合
     */
    private <T> List<T> iterToList(Iterator<T> iter) {
        List<T> list = new ArrayList<>();
        while (iter.hasNext()) {
            list.add(iter.next());
        }
        return list;
    }

    /*
    构建寻找请求
     */
    private FindRequest buildFindRequest(String targetId) {
        return FindRequest.newBuilder()
                .setNodeInfo(nodeInfo)
                .setTargetId(targetId)
                .build();
    }

    /*
    构建阻塞客户端
     */
    private KademliaServiceGrpc.KademliaServiceBlockingStub buildBlockClient(String host, int port) {
        return KademliaServiceGrpc.newBlockingStub(buildChannel(host, port));
    }

    /*
    构建通道
     */
    private ManagedChannel buildChannel(String host, int port) {
        return ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
    }

}
