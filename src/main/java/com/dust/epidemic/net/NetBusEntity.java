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

    private String uri;

    public static NetBusEntity createPush(Node node, NetMessage netMessage) {
        NetBusEntity entity = new NetBusEntity();
        entity.setNode(node);
        entity.setNetMessage(netMessage);
        entity.setUri(node.getAddress() + ":" + node.getPort() + NetConstant.PUSH_URI);
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
