package com.dust.epidemic;

import com.dust.epidemic.core.EpidemicNode;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class EpidemicTest {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(EpidemicNode.class.getName(), new DeploymentOptions().setConfig(new JsonObject().put("port", 8080)));
        vertx.deployVerticle(EpidemicNode.class.getName(), new DeploymentOptions().setConfig(new JsonObject().put("port", 8081)));
        vertx.deployVerticle(EpidemicNode.class.getName(), new DeploymentOptions().setConfig(new JsonObject().put("port", 8082)));
    }

}
