package com.dust.router;

import com.dust.grpc.kademlia.FindNodeResponse;
import com.dust.grpc.kademlia.FindValueResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 节点的三元组表示
 */
@Setter
@Getter
public class NodeTriad {

    private String key;

    private String host;

    private int port;

    public NodeTriad(String nodeId, String host, int port) {
        this.key = nodeId;
        this.host = host;
        this.port = port;
    }

    public FindNodeResponse toFindNodeResponse() {
        return FindNodeResponse.newBuilder()
                .setCode(1)
                .setHost(host)
                .setNodeId(key)
                .setPort(port)
                .build();
    }

    public FindValueResponse toFindValueResponse() {
        return FindValueResponse.newBuilder()
                .setCode(1)
                .setMode(2)
                .setHost(host)
                .setPort(port)
                .setNodeId(key)
                .build();
    }

}
