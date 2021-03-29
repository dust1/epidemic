package com.dust.epidemic.handlers;

import com.dust.epidemic.core.DataManager;
import com.dust.epidemic.core.NodeManager;
import com.dust.epidemic.core.NodeView;
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

    private DataManager dataManager;

    public ActiveHandler(Vertx vertx, NodeManager nodeManager, DataManager dataManager) {
        this.vertx = vertx;
        this.nodeManager = nodeManager;
        this.dataManager = dataManager;
    }



    /**
     * 从现有的节点列表中随机获取并发送数据
     * push 与其他节点交换数据并更新
     * @param message 发送过来的数据
     */
    public void sendPushHandler(Message<NetMessage> message) {
        NetMessage netMessage = message.body();

        List<Node> nodes = nodeManager.selectPeer(netMessage.getSourceAddress(), netMessage.getAddress());
        if (!nodes.isEmpty()) {
            Descriptor descriptor = nodeManager.getMyDescriptor();
            NodeView nodeView = dataManager.getView();
            NetMessage pushBuffer = NetMessage.merge(nodeView, descriptor, netMessage.getSourceAddress());
            nodes.forEach(node -> vertx.eventBus().send("net-send", NetBusEntity.create(node, pushBuffer)));
        }
    }

    /**
     * pull 转发最新的更新到其他节点
     * @param message 发送过来的数据
     */
    public void sendPullHandler(Message<NetMessage> message) {
        NetMessage netMessage = message.body();

        List<Node> nodes = nodeManager.selectPeer(netMessage.getSourceAddress(), netMessage.getAddress());
        if (!nodes.isEmpty()) {
            netMessage.getAddress().increase();
            netMessage.getSourceAddress().increase();

            nodes.forEach(node -> vertx.eventBus().send("net-send", NetBusEntity.create(node, netMessage)));
        }
    }

}
