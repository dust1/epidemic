package com.dust.epidemic.handlers;

import com.dust.epidemic.data.DataManager;
import com.dust.epidemic.core.NodeManager;
import com.dust.epidemic.data.NodeView;
import com.dust.epidemic.net.Descriptor;
import com.dust.epidemic.net.NetBusEntity;
import com.dust.epidemic.net.NetMessage;
import com.dust.epidemic.net.Node;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;

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
     * push 转发最新的更新到其他节点
     * @param message 发送过来的数据
     */
    public void sendPushHandler(Message<String> message) {
        String messageJson = message.body();
        NetMessage netMessage = Json.decodeValue(messageJson, NetMessage.class);
        List<Node> nodes = nodeManager.selectPeer(netMessage.getSourceAddress(), netMessage.getAddress());
        System.out.println("节点：" + nodeManager.getMyDescriptor().toString() + "检索转发节点：" + nodes.toString());
        if (!nodes.isEmpty()) {
            /* 将自己的描述添加到节点信息中 */
            Descriptor descriptor = nodeManager.getMyDescriptor();
            netMessage.setAddress(descriptor);

            netMessage.getSourceAddress().increase();
            nodes.forEach(node -> vertx.eventBus().send("net-send", Json.encode(NetBusEntity.createPush(node, netMessage))));
        }
    }

    /**
     * pull 与其他节点交换数据并更新
     * @param message 发送过来的数据
     */
    public void sendPullHandler(Message<NetMessage> message) {
        NetMessage netMessage = message.body();

        List<Node> nodes = nodeManager.selectPeer(netMessage.getSourceAddress(), netMessage.getAddress());
        if (!nodes.isEmpty()) {
//            Descriptor descriptor = nodeManager.getMyDescriptor();
//
//            netMessage.getSourceAddress().increase();
//            netMessage.setAddress(descriptor);
//            nodes.forEach(node -> vertx.eventBus().send("net-send", NetBusEntity.createPull(node, netMessage)));
        }
    }

}
