package com.dust.epidemic.handlers;

import com.dust.epidemic.net.NetBusEntity;
import com.dust.epidemic.net.NetMessage;
import com.dust.epidemic.net.Node;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpClient;

/**
 * 客户端对内部消息处理
 */
public class ClientHandler {

    private HttpClient client;

    public ClientHandler(HttpClient client) {
        this.client = client;
    }

    /**
     * http客户端发送对象
     * @param message
     */
    public void send(Message<NetBusEntity> message) {
        NetBusEntity netBusEntity = message.body();
        Node sendNode = netBusEntity.getNode();
        NetMessage sendMessage = netBusEntity.getNetMessage();

        //TODO 需要查询资料，vertx http client如何采用post发送数据对象
//        client.post()
    }

}
