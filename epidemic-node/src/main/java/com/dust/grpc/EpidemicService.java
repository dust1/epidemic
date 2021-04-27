package com.dust.grpc;

import com.dust.grpc.kademlia.KademliaServiceGrpc;
import com.dust.router.RouterLayout;
import com.dust.storage.StorageLayout;
import io.grpc.stub.StreamObserver;

/**
 * 网络服务
 */
public class EpidemicService extends KademliaServiceGrpc.KademliaServiceImplBase {

    //存储层与网络模块对接接口
    private final StorageLayout storageLayout;

    //路由层与网络模块对接接口
    private final RouterLayout routerLayout;

    /**
     * 网络模块，对接着存储模块与路由模块
     */
    public EpidemicService(StorageLayout storageLayout, RouterLayout routerLayout) {
        this.storageLayout = storageLayout;
        this.routerLayout = routerLayout;
    }

    @Override
    public void ping(com.dust.grpc.kademlia.PingPackage request, StreamObserver<com.dust.grpc.kademlia.PingPackage> responseObserver) {
        super.ping(request, responseObserver);
    }

    @Override
    public void store(com.dust.grpc.kademlia.StoreRequest request, StreamObserver<com.dust.grpc.kademlia.StoreResponse> responseObserver) {
        super.store(request, responseObserver);
    }

    @Override
    public void findNode(com.dust.grpc.kademlia.FindRequest request, StreamObserver<com.dust.grpc.kademlia.FindNodeResponse> responseObserver) {
        super.findNode(request, responseObserver);
    }

    @Override
    public void findValue(com.dust.grpc.kademlia.FindRequest request, StreamObserver<com.dust.grpc.kademlia.FindValueResponse> responseObserver) {
        super.findValue(request, responseObserver);
    }
}
