package com.dust.epidemic.handlers;

import com.dust.epidemic.core.NodeManager;
import com.dust.epidemic.net.Descriptor;
import com.dust.epidemic.net.NetBusEntity;
import com.dust.epidemic.net.NetMessage;
import com.dust.epidemic.net.Node;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;

import java.util.List;

/**
 * 活动处理器
 */
public class ActiveHandler {

    private Vertx vertx;

    private NodeManager nodeManager;

    public ActiveHandler(Vertx vertx, NodeManager nodeManager) {
        this.vertx = vertx;
        this.nodeManager = nodeManager;
    }



    /**
     * 从现有的节点列表中随机获取并发送数据
     * @param message
     */
    public void sendHandler(Message<NetMessage> message) {
        NetMessage netMessage = message.body();

        List<Node> nodes = nodeManager.selectPeer(netMessage.getSourceAddress(), netMessage.getAddress());
        if (!nodes.isEmpty()) {
            Descriptor descriptor = nodeManager.getMyDescriptor();
            NetMessage pushBuffer = netMessage.merge(netMessage.getView(), descriptor);
            nodes.forEach(node -> vertx.eventBus().send("net-send", NetBusEntity.create(node, pushBuffer)));
        }
    }

}
