package com.dust.grpc;

import com.dust.grpc.kademlia.FindValueResponse;
import com.dust.grpc.kademlia.KademliaServiceGrpc;
import com.dust.grpc.kademlia.PingPackage;
import com.dust.grpc.kademlia.StoreResponse;
import com.dust.router.FindValueResult;
import com.dust.router.KademliaRouterLayout;
import com.dust.router.NodeTriad;
import com.dust.router.RouterLayout;
import com.dust.storage.StorageLayout;
import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Optional;

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
        PingPackage result =request.toBuilder().setNodeId(routerLayout.getMyId())
                .setTimestamp(request.getTimestamp())
                .build();
        responseObserver.onNext(result);
        responseObserver.onCompleted();
    }

    @Override
    public void store(com.dust.grpc.kademlia.StoreRequest request, StreamObserver<com.dust.grpc.kademlia.StoreResponse> responseObserver) {
        StoreResponse response;
        try {
            response = storageLayout.store(request);
        } catch (IOException e) {
            response = StoreResponse.newBuilder()
                    .setCode(0)
                    .setErrmsg(e.getMessage())
                    .build();
        }

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void findNode(com.dust.grpc.kademlia.FindRequest request, StreamObserver<com.dust.grpc.kademlia.FindNodeResponse> responseObserver) {
        List<NodeTriad> list = routerLayout.findNode(request.getKey());
        list.forEach(node -> responseObserver.onNext(node.toFindNodeResponse()));
        responseObserver.onCompleted();
    }

    @Override
    public void findValue(com.dust.grpc.kademlia.FindRequest request, StreamObserver<com.dust.grpc.kademlia.FindValueResponse> responseObserver) {
        try {
            Optional<ByteBuffer> fileOptional = storageLayout.find(request.getKey());
            if (fileOptional.isPresent()) {
                //有文件，返回文件信息
                responseObserver.onNext(
                        FindValueResponse.newBuilder()
                                .setCode(1)
                                .setMode(1)
                                .setData(ByteString.copyFrom(fileOptional.get()))
                                .build()
                );
            } else {
                List<NodeTriad> list = routerLayout.findNode(request.getKey());
                list.forEach(node -> responseObserver.onNext(node.toFindValueResponse()));
            }
        } catch (IOException e) {
            //记录日志，返回错误信息
            responseObserver.onNext(
                    FindValueResponse.newBuilder()
                            .setCode(0)
                            .setErrmsg(e.getMessage())
                            .build()
            );
        }

        responseObserver.onCompleted();
    }
}
