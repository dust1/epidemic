package com.dust.router.kademlia.timer;

import com.dust.fundation.EpidemicUtils;
import com.dust.grpc.kademlia.KademliaServiceGrpc;
import com.dust.grpc.kademlia.NodeInfo;
import com.dust.grpc.kademlia.PingRequest;
import com.dust.grpc.kademlia.PingResponse;
import com.dust.guard.Task;
import com.dust.router.kademlia.KademliaBucket;
import com.dust.router.kademlia.NodeTriadRouterNode;
import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.ManagedChannelBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * 测试所有路由节点网络情况的任务
 */
public class PingTask implements Task {

    private KademliaBucket bucket;

    public PingTask(KademliaBucket bucket) {
        this.bucket = bucket;
    }

    @Override
    public void process() {
        var nodes = bucket.cloneBucket();
        var cacheNodes = bucket.cloneCache().stream()
                .collect(Collectors.toMap(node -> EpidemicUtils.getDis(node.getKey(), bucket.myNode),
                        Collections::singletonList,
                        (k1, k2) -> {
                            var list = new ArrayList<>(k1);
                            list.addAll(k2);
                            return list;
                        }));

        var result = nodes.parallelStream()
                .map(node -> {
                    var channel = ManagedChannelBuilder
                            .forAddress(node.getHost(), node.getPort())
                            .usePlaintext()
                            .build();
                    var client = KademliaServiceGrpc.newFutureStub(channel);
                    int timestamp = (int) (System.currentTimeMillis() / 1000);
                    var nodeInfo = NodeInfo.newBuilder()
                            .setNodeId(bucket.myNode)
                            .setPort(bucket.getPort())
                            .build();
                    var req = PingRequest.newBuilder()
                            .setTimestamp(timestamp)
                            .setNodeInfo(nodeInfo)
                            .build();
                    var future = client.ping(req);
                    try {
                        future.get(10, TimeUnit.SECONDS);
                        return node;
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        result.forEach(failNode -> {
            int failDis = EpidemicUtils.getDis(failNode.getKey(), bucket.myNode);
            var cacheList = cacheNodes.get(failDis);
            if (Objects.isNull(cacheList) || cacheList.isEmpty()) {
                return;
            }

            var newNode = cacheList.get(0);
            bucket.remove(failNode);
            bucket.add(newNode);
            bucket.removeCache(newNode);
            cacheList.remove(0);
        });
    }
}
