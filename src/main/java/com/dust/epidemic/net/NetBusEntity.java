package com.dust.epidemic.net;

import lombok.Getter;
import lombok.Setter;

/**
 * 内部用于网络发送的实体
 */
@Getter
@Setter
public class NetBusEntity {

    private Node node;

    private NetMessage netMessage;

    private int port;
    private String uri;
    private String host;

    public static NetBusEntity createPush(Node node, NetMessage netMessage) {
        NetBusEntity entity = new NetBusEntity();
        entity.setNode(node);
        entity.setNetMessage(netMessage);

        entity.setPort(node.getPort());
        entity.setHost(node.getAddress());
        entity.setUri(NetConstant.PUSH_URI);
        return entity;
    }

    public static NetBusEntity createPull(Node node, NetMessage netMessage) {
        NetBusEntity entity = new NetBusEntity();
        entity.setNode(node);
        entity.setNetMessage(netMessage);
        entity.setUri(node.getAddress() + ":" + node.getPort() + NetConstant.PULL_URI);
        return entity;
    }

}
