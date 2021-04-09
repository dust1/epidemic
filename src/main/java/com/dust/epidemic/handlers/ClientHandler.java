package com.dust.epidemic.handlers;

import com.dust.epidemic.net.NetBusEntity;
import com.dust.epidemic.net.NetMessage;
import com.dust.epidemic.net.Node;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.Json;

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
        NetMessage sendMessage = netBusEntity.getNetMessage();
        client.post(netBusEntity.getUri()).putHeader("content-type", "application/json").end(Json.encodeToBuffer(sendMessage));
    }

}
