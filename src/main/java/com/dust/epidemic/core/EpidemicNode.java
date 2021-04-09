package com.dust.epidemic.core;

import com.dust.epidemic.data.DataManager;
import com.dust.epidemic.handlers.ActiveHandler;
import com.dust.epidemic.handlers.ClientHandler;
import com.dust.epidemic.handlers.RequestHandler;
import com.dust.epidemic.net.NetConstant;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

public class EpidemicNode extends AbstractVerticle {

    @Override
    public void start() {
//        vertx.fileSystem().readFile("./conf/setting.json", settingRes -> {
//            if (settingRes.succeeded()) {
//
//            } else {
//                System.out.println("read setting.json fail");
//            }
//        });

        HttpServer server = vertx.createHttpServer();
        HttpClient client = vertx.createHttpClient();

        Router router = Router.router(vertx);

        DataManager dataManager = new DataManager("path");
        NodeManager nodeManager = new NodeManager("0.0.0.0", 8080);

        ClientHandler clientHandler = new ClientHandler(client);
        RequestHandler requestHandler = new RequestHandler(nodeManager, dataManager, vertx);
        ActiveHandler activeHandler = new ActiveHandler(vertx, nodeManager, dataManager);

        setRouter(router, requestHandler);
        server.requestHandler(router).listen(8080, serverRes -> {
            if (serverRes.succeeded()) {

                vertx.eventBus().consumer("push-ready", activeHandler::sendPushHandler);
                vertx.eventBus().consumer("pull-ready", activeHandler::sendPullHandler);

                vertx.eventBus().consumer("net-send", clientHandler::send);

                System.out.println("node start ok");
            } else {
                System.out.println("node start fail");
                serverRes.cause().printStackTrace();
            }
        });
    }

    /**
     * 配置路由表
     */
    private void setRouter(Router router, RequestHandler handler) {
        router.route(HttpMethod.POST, NetConstant.PUSH_URI).handler(handler::push);
        router.route(HttpMethod.POST, NetConstant.PULL_URI).handler(handler::pull);
    }

}
