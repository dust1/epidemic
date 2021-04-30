package com.dust.router;

import lombok.Getter;
import lombok.Setter;

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

}
