package com.dust.epidemic.handlers;

import com.dust.epidemic.data.DataManager;
import com.dust.epidemic.core.NodeManager;
import com.dust.epidemic.net.NetMessage;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

/**
 * 请求处理器
 */
public class RequestHandler {

    private DataManager dataManager;
    private NodeManager nodeManager;
    private Vertx vertx;

    public RequestHandler(NodeManager nodeManager, DataManager dataManager, Vertx vertx) {
        this.nodeManager = nodeManager;
        this.dataManager = dataManager;
        this.vertx = vertx;
    }

    /**
     * 收到push请求
     * push - 表示有新数据
     * @param context
     */
    public void push(RoutingContext context) {
        context.request().bodyHandler(buffer -> {
            NetMessage message = Json.decodeValue(buffer, NetMessage.class);
            dataManager.merge(message.getView());
            System.out.println(message.logInfo());

            //将自身的数据转发出去
            vertx.eventBus().send("push-ready", message);
            context.response().end("{\"status\":1}");
        });
    }

    /**
     * 收到pull请求
     * pull - 表示要和请求节点互相认证数据
     * @param context
     */
    public void pull(RoutingContext context) {
        context.request().bodyHandler(buffer -> {
            NetMessage message = Json.decodeValue(buffer, NetMessage.class);
            dataManager.merge(message.getView());
            System.out.println(message.logInfo());

            vertx.eventBus().send("pull-ready", message);
            context.response().end("{\"status\":1}");
        });
    }

}
