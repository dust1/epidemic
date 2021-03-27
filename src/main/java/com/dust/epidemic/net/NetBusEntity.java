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

    public static NetBusEntity create(Node node, NetMessage netMessage) {
        NetBusEntity entity = new NetBusEntity();
        entity.setNode(node);
        entity.setNetMessage(netMessage);
        return entity;
    }

}
