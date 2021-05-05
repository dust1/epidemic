package com.dust;

import com.dust.core.NodeConfig;
import com.dust.fundation.StartedFunction;
import com.dust.grpc.EpidemicService;
import com.dust.router.KademliaRouterLayout;
import com.dust.router.RouterLayout;
import com.dust.storage.FileStorageLayout;
import com.dust.storage.StorageLayout;
import io.grpc.Server;
import io.grpc.ServerBuilder;

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
        this.storageLayout = new FileStorageLayout(config);
        this.routerLayout = new KademliaRouterLayout(config);
        this.server = ServerBuilder.forPort(nodeConfig.getPort())
                .addService(new EpidemicService(storageLayout, routerLayout))
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
        storageLayout.load();
        routerLayout.load();

        server.start();
        rollback.apply();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Use stderr here since the logger may have been reset by its JVM shutdown hook.
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            try {
                EpidemicServer.this.stop();
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
            System.err.println("*** server shut down");
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
     * 立刻结束服务，直到服务结束前线程等待
     */
    public void shutdownNow() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

}
