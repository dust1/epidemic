package com.dust.grpc;

import com.dust.grpc.kademlia.FindValueResponse;
import com.dust.grpc.kademlia.KademliaServiceGrpc;
import com.dust.grpc.kademlia.PingResponse;
import com.dust.grpc.kademlia.StoreResponse;
import com.dust.router.kademlia.NodeTriad;
import com.dust.router.RouterLayout;
import com.dust.storage.StorageLayout;
import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Objects;
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

    /**
     * ping。当收到一个节点的ping请求，并且发现自身节点某个文件与这个节点的距离比自身的距离还要近的时候，将这个文件块推送到对应的节点中
     */
    @Override
    public void ping(com.dust.grpc.kademlia.PingRequest request, StreamObserver<com.dust.grpc.kademlia.PingResponse> responseObserver) {
        String nodeId = request.getNodeInfo().getNodeId();
        String clientIp = ClientAddressInterceptor.CLIENT_ADDRESS.get();
        int port = request.getNodeInfo().getPort();
        routerLayout.ping(nodeId, clientIp, port);
        storageLayout.haveNewNode(nodeId, clientIp, port);
        PingResponse res = PingResponse.newBuilder()
                .setTimestamp(request.getTimestamp())
                .build();
        responseObserver.onNext(res);
        responseObserver.onCompleted();
    }

    @Override
    public void store(com.dust.grpc.kademlia.StoreRequest request, StreamObserver<com.dust.grpc.kademlia.StoreResponse> responseObserver) {
        var clientHost = ClientAddressInterceptor.CLIENT_ADDRESS.get();
        routerLayout.ping(request.getNodeInfo(), clientHost);

        StoreResponse response;
        try {
            var data = request.getData();
            var fileId = request.getFileId();
            var buffer = data.asReadOnlyByteBuffer();
            storageLayout.store(buffer, fileId);
            response = StoreResponse.newBuilder()
                        .setCode(1)
                        .build();
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
        var clientHost = ClientAddressInterceptor.CLIENT_ADDRESS.get();
        routerLayout.ping(request.getNodeInfo(), clientHost);

        List<NodeTriad> list = routerLayout.findNode(request.getTargetId());
        list.forEach(node -> responseObserver.onNext(node.toFindNodeResponse()));
        responseObserver.onCompleted();
    }

    @Override
    public void findValue(com.dust.grpc.kademlia.FindRequest request, StreamObserver<com.dust.grpc.kademlia.FindValueResponse> responseObserver) {
        var clientHost = ClientAddressInterceptor.CLIENT_ADDRESS.get();
        routerLayout.ping(request.getNodeInfo(), clientHost);

        try {
            ByteBuffer fileOptional = storageLayout.findFile(request.getTargetId());
            if (Objects.nonNull(fileOptional)) {
                //有文件，返回文件信息
                responseObserver.onNext(
                        FindValueResponse.newBuilder()
                                .setCode(1)
                                .setMode(1)
                                .setData(ByteString.copyFrom(fileOptional))
                                .build()
                );
            } else {
                List<NodeTriad> list = routerLayout.findNode(request.getTargetId());
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
