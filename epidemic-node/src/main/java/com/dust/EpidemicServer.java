package com.dust;

import com.dust.fundation.StartedFunction;
import com.dust.grpc.ClientAddressInterceptor;
import com.dust.grpc.EpidemicService;
import com.dust.logs.LogFormat;
import com.dust.logs.Logger;
import com.dust.router.kademlia.KademliaRouterLayout;
import com.dust.router.RouterLayout;
import com.dust.scheduler.RePublishing;
import com.dust.storage.FileStorageLayout;
import com.dust.storage.StorageLayout;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptors;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 当前节点服务主类
 */
public class EpidemicServer {

    private final NodeConfig config;
    private final Server server;

    private final StorageLayout storageLayout;
    private final RouterLayout routerLayout;

    public static EpidemicServer create(NodeConfig config) throws IOException {
        return new EpidemicServer(config);
    }

    private EpidemicServer(NodeConfig nodeConfig) throws IOException {
        this.config = nodeConfig;

        this.routerLayout = KademliaRouterLayout.create(config);
        this.storageLayout = FileStorageLayout.create(config, routerLayout.getMyId());
        this.server = ServerBuilder.forPort(nodeConfig.getNodePort())
                .addService(ServerInterceptors.intercept(
                        new EpidemicService(storageLayout, routerLayout),
                        new ClientAddressInterceptor()
                ))
                .build();
    }

    public void start() throws IOException {
        start(() -> {});
    }

    /**
     * 这个函数通常用于测试进行线程栅栏的时候使用
     * @param rollback 启动完成后的回调接口
     */
    public void start(StartedFunction rollback) throws IOException {
        //TODO 启动前需要加载路由表以及索引文件索引
        routerLayout.before();
        storageLayout.before();
        server.start();

        rollback.apply();
        Logger.systemLog.info(LogFormat.SYSTEM_INFO_FORMAT, "节点启动完成...");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Logger.systemLog.info(LogFormat.SYSTEM_INFO_FORMAT, "*** shutting down gRPC server since JVM is shutting down");
            try {
                EpidemicServer.this.stop();
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
            Logger.systemLog.info(LogFormat.SYSTEM_INFO_FORMAT, "*** server shut down");
        }));
    }

    /**
     * 关闭服务
     * @throws InterruptedException
     *      定时配置失败
     */
    private void stop() throws InterruptedException {
        if (Objects.nonNull(server)) {
            server.shutdown()
                    .awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    /**
     * 阻塞，直到虚拟机关闭
     */
    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

}
