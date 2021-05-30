package com.dust.router.kademlia;

import com.dust.NodeConfig;
import com.dust.fundation.EpidemicUtils;
import com.dust.logs.LogFormat;
import com.dust.logs.Logger;

import java.util.*;

/**
 * Kademlia的路由桶管理器，管理所有Bucket的生成/复制/分裂
 */
public class KademliaBucket {

    /**
     * 每个bucket存放节点数量
     */
    private final int bucketKey;

    /**
     * 当新增节点达到saveSize数量后持久化到磁盘中
     */
    private final int saveSize;

    private final String myNode;

    /**
     * 通过使用时间进行排序的桶快照
     */
    private Bucket nodeCache;

    /**
     * 存放路由信息的桶
     */
    private Bucket[] buckets;

    /**
     * 节点数
     */
    private int size;

    /**
     * 短时间内的新增节点数
     * 当这个数字超过saveSize后触发持久化操作
     */
    private int addCount;

    public KademliaBucket(NodeConfig config, String myNode) {
        this.bucketKey = config.getBucketKey();
        this.myNode = myNode;
        this.size = 0;
        this.saveSize = config.getRouterSaveCount();
        init();
    }

    /**
     * 清空桶
     * 将桶中的所有节点数据全部清除
     */
    public void clear() {
        init();
    }

    private void init() {
        this.buckets = new Bucket[160];
        this.nodeCache = new Bucket(bucketKey);
        this.size = 0;

        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new Bucket(bucketKey);
        }
    }

    /**
     * 往桶中添加一个路由表
     * @param node 要添加的路由信息
     */
    public void add(NodeTriadRouterNode node) {
        if (node.getKey().equals(myNode)) {
            return;
        }
        int disIndex = EpidemicUtils.getDis(node.getKey(), myNode);
        var bucket = buckets[disIndex];
        if (bucket.contains(node.getKey())) {
            return;
        }
        Logger.layoutLog.info(LogFormat.LAYOUT_ADD_CACHE_FORMAT, node.getHost(), node.getPort(), node.getKey());
        if (bucket.size() >= bucketKey) {
            //bucket数量超过k，将其暂时加入cache中
            nodeCache.add(node);
        } else {
            bucket.add(node);
            size += 1;
            addCount += 1;
        }
    }

    /**
     * 有一个节点进行ping操作，检查本地路由表中是否有该节点，如果有则更新他的updateTime，如果没有则追加
     * @param nodeId 发起ping的节点id
     * @param host 发起ping的节点ip
     * @param port 发起ping的节点端口
     */
    public void checkNewNode(String nodeId, String host, int port) {
        if (nodeId.equals(myNode)) {
            return;
        }
        int disIndex = EpidemicUtils.getDis(nodeId, myNode);
        var bucket = buckets[disIndex];

        if (bucket.contains(nodeId)) {
            bucket.refreshNode(nodeId);
            return;
        }

        if (nodeCache.contains(nodeId)) {
            nodeCache.refreshNode(nodeId);
            return;
        }

        //都不存在表示这是一个新的节点,创建
        var node = new NodeTriadRouterNode(nodeId, host, port);
        add(node);
    }

    /**
     * 根据给定的nodeId，查询跟这个nodeId接近的k个节点三元组
     * @param nodeId 要查询的nodeId
     * @return 与这个nodeId接近的节点三元组集合
     */
    public List<NodeTriad> findNode(String nodeId) {
        List<NodeTriad> result = new ArrayList<>(bucketKey);
        int disIndex = EpidemicUtils.getDis(nodeId, myNode);

        Queue<Integer> indexQueue = new LinkedList<>();
        indexQueue.add(disIndex);
        boolean[] used = new boolean[160];
        while (!indexQueue.isEmpty() && result.size() < bucketKey) {
            Integer index = indexQueue.poll();
            if (index < 0 || index >= used.length || used[index]) {
                continue;
            }
            var bucket = buckets[index];
            result.addAll(bucket.cloneNode());
            used[index] = true;
            indexQueue.add(index + 1);
            indexQueue.add(index - 1);
        }
        return result;
    }

    /**
     * 判断桶中是否有当给定节点id对应的路由节点
     * @param nodeId 给定的节点id
     * @return 如果存在则返回true，否则返回false
     */
    public boolean contains(String nodeId) {
        int disIndex = EpidemicUtils.getDis(nodeId, myNode);
        var bucket = buckets[disIndex];
        return bucket.contains(nodeId) || nodeCache.contains(nodeId);
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    /**
     * 移除节点
     * @param nodeId 要移除的路由节点id
     */
    public void remove(String nodeId) {
        int disIndex = EpidemicUtils.getDis(nodeId, myNode);
        var bucket = buckets[disIndex];
        if (bucket.remove(nodeId))
            size -= 1;

        nodeCache.remove(nodeId);
    }

    /**
     * 检查该节点是否需要持久化
     */
    public boolean canSave() {
        if (addCount < saveSize) {
            return false;
        }
        addCount = 0;
        return true;
    }

    /**
     * 复制buckets中的所有节点数据
     */
    public List<NodeTriadRouterNode> cloneBucket() {
        var list = new ArrayList<NodeTriadRouterNode>(size);
        for (var bucket : buckets) {
            list.addAll(bucket.cloneNode());
        }
        return list;
    }

}
