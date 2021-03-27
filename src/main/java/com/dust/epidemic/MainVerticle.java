package com.dust.epidemic;

import com.dust.epidemic.core.EpidemicNode;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        vertx.deployVerticle(EpidemicNode.class.getName());
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(EpidemicNode.class.getName());
    }

}
