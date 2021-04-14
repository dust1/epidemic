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
     * 这里是通过客户端上传的数据信息
     * 用户只是上传文件，客户端会计算出文件的MD5码作为文件的key
     */
    public void pushByClient() {
        //TODO 缓存文件，并检索集群查询是否有空余节点能够存放这个文件。

    }

    /**
     * 这里是从其他节点收到push请求
     * push - 表示有新数据
     * @param context
     */
    public void push(RoutingContext context) {
        context.request().bodyHandler(buffer -> {
            NetMessage message = Json.decodeValue(buffer, NetMessage.class);
            if (dataManager.check(message.getTimestamp())) {
                System.out.println("节点：" + nodeManager.getMyDescriptor().toString() + "收到push请求：" + message.toString());
//                dataManager.merge(message.getKey(), message.getValue());
                //将自身的数据转发出去
                vertx.eventBus().send("push-ready", Json.encode(message));
            }
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
//            dataManager.merge(message.getKey(), message.getValue());
            vertx.eventBus().send("pull-ready", message);
            context.response().end("{\"status\":1}");
        });
    }

}
