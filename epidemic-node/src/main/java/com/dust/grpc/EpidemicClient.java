package com.dust.grpc;

import com.dust.grpc.kademlia.KademliaServiceGrpc;
import com.dust.grpc.kademlia.PingRequest;
import io.grpc.Channel;

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

}
