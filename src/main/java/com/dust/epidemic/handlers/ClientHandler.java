package com.dust.epidemic.handlers;

import com.dust.epidemic.net.NetBusEntity;
import com.dust.epidemic.net.NetMessage;
import com.dust.epidemic.net.Node;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
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
    public void send(Message<String> message) {
        String jsonStr = message.body();
        NetBusEntity netBusEntity = Json.decodeValue(jsonStr, NetBusEntity.class);
        NetMessage sendMessage = netBusEntity.getNetMessage();
        Buffer buffer = Json.encodeToBuffer(sendMessage);

        HttpClientRequest request = client.post(netBusEntity.getPort(), netBusEntity.getHost(), netBusEntity.getUri());
        request.handler(res -> {
            System.out.println("ok");
        });
        request.putHeader("content-type", "application/json");
        request.putHeader("content-length", "" + buffer.length());
        request.write(buffer);
        request.end();
    }

}
