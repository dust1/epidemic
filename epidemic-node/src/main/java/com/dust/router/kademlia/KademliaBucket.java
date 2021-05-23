package com.dust.router.kademlia;

import com.dust.NodeConfig;
import com.dust.fundation.EpidemicUtils;
import com.dust.logs.LogFormat;
import com.dust.logs.Logger;
import com.dust.router.kademlia.timer.KademliaRouterTimer;

import java.util.*;

/**
 * Kademlia的路由桶管理器，管理所有Bucket的生成/复制/分裂
 */
public class KademliaBucket {

    private int k;

    public String myNode;

    private int bucketSize;

    /**
     * 通过使用时间进行排序的桶快照
     */
    private PriorityQueue<NodeTriadRouterNode> nodeCache;

    /**
     * 存放路由信息的桶
     */
    private List<NodeTriadRouterNode>[] buckets;

    /**
     * 桶相关的定时任务
     * 包括：1、定时将内存数据持久化到磁盘；2、定时检测桶中的路由节点通信是否顺畅，如果不是则将其删除
     */
    private KademliaRouterTimer timer;

    public final NodeConfig config;

    public KademliaBucket(NodeConfig config, String myNode) {
        this.k = config.getBucketKey();
        this.myNode = myNode;
        this.bucketSize = 1;
        this.config = config;

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
        this.buckets = new List[160];
        this.nodeCache = new PriorityQueue<>(k, (n1, n2) -> Integer.compare(n2.getUpdateTime(), n1.getUpdateTime()));
        this.buckets[0] = new ArrayList<>(k);
    }

    /**
     * 初始化bucket的定时任务
     */
    public void initTimer(NodeConfig config) {
        this.timer = new KademliaRouterTimer(config);
//        this.timer.start(this);
    }

    /**
     * 开启定时任务
     */
    public void startTimer() {
        this.timer.start(this);
    }

    /**
     * 往桶中添加一个路由表
     * @param node 要添加的路由信息
     */
    public void add(NodeTriadRouterNode node) {
        if (node.getKey().equals(myNode)) {
            return;
        }
        int prevIndex = EpidemicUtils.getDis(node.getKey(), myNode);
        prevIndex = prevIndex >= bucketSize ? bucketSize - 1 : prevIndex;
        var bucket = buckets[prevIndex];
        for (var n : bucket) {
            if (n.getKey().equals(node.getKey())) {
                return;
            }
        }

        if (bucket.size() >= k) {
            //拆分桶。两种情况：1.存在后续桶。将新节点放入cache中等待垃圾回收进行重新排布；2.不存在后续桶。创建新桶
            if ((prevIndex + 1) < bucketSize) {
                Logger.layoutLog.info(LogFormat.LAYOUT_ADD_CACHE_FORMAT, node.getHost(), node.getPort(), node.getKey());
                //一存在下一个桶，放入cache中
                for (NodeTriadRouterNode haveNode : nodeCache) {
                    if (haveNode.getKey().equals(node.getKey())) {
                        nodeCache.remove(haveNode);
                        break;
                    }
                }
                node.updateTime();
                nodeCache.add(node);
            } else {
                Logger.layoutLog.info(LogFormat.LAYOUT_ADD_FORMAT, node.getHost(), node.getPort(), node.getKey());
                var newBucket = new ArrayList<NodeTriadRouterNode>(k);
                var oldBucket = new ArrayList<NodeTriadRouterNode>(k);
                for (var n : bucket) {
                    int index = EpidemicUtils.getDis(n.getKey(), myNode);
                    if (index == prevIndex) {
                        oldBucket.add(n);
                    } else if (index > prevIndex) {
                        newBucket.add(n);
                    }
                }
                buckets[prevIndex] = oldBucket;
                if ((prevIndex + 1) < buckets.length) {
                    buckets[prevIndex + 1] = newBucket;
                    bucketSize += 1;
                }
            }
        } else {
            Logger.layoutLog.info(LogFormat.LAYOUT_ADD_FORMAT, node.getHost(), node.getPort(), node.getKey());
            //不拆分桶，直接插入
            bucket.add(node);
        }

        timer.add();
    }



    /**
     * 有一个节点进行ping槽走，检查本地路由表中是否有该节点，如果有则更新他的updatetime，如果没有则追加
     * @param nodeId 发起ping的节点id
     * @param host 发起ping的节点ip
     * @param port 发起ping的节点端口
     */
    public void ping(String nodeId, String host, int port) {
        if (nodeId.equals(myNode)) {
            return;
        }
        int index = EpidemicUtils.getDis(nodeId, myNode);
        index = index >= bucketSize ? bucketSize - 1 : index;
        var bucket = buckets[index];

        //如果路由表中存在
        for (var node : bucket) {
            if (node.getKey().equals(nodeId)) {
                node.updateTime();
                return;
            }
        }

        //如果缓存队列中存在
        for (var node : nodeCache) {
            if (node.getKey().equals(nodeId)) {
                nodeCache.remove(node);
                node.updateTime();
                nodeCache.add(node);
                return;
            }
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
        List<NodeTriad> result = new ArrayList<>(k);
        int startIndex = EpidemicUtils.getDis(nodeId, myNode);
        startIndex = startIndex >= bucketSize ? bucketSize - 1 : startIndex;

        Queue<Integer> indexQueue = new LinkedList<>();
        indexQueue.add(startIndex);
        boolean[] used = new boolean[160];
        while (!indexQueue.isEmpty() && result.size() < k) {
            Integer index = indexQueue.poll();
            if (index < 0 || index >= used.length || used[index] || Objects.isNull(buckets[index])) {
                continue;
            }
            var bucket = buckets[index];
            for (int i = 0; i < bucket.size() && result.size() < k; i++) {
                var node = bucket.get(i);
                result.add(node);
            }
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
        int startIndex = EpidemicUtils.getDis(nodeId, myNode);
        startIndex = startIndex >= bucketSize ? bucketSize - 1 : startIndex;

        var bucket = buckets[startIndex];
        if (Objects.isNull(bucket) || bucket.isEmpty()) {
            return false;
        }

        return bucket.stream().anyMatch(node -> node.getKey().equals(nodeId));
    }

    /**
     * 将桶中的数据完整克隆出来
     */
    public synchronized List<NodeTriadRouterNode> cloneBucket() {
        var result = new ArrayList<NodeTriadRouterNode>();
        for (int i = 0; i < bucketSize; i++) {
            var bucket = buckets[i];
            result.addAll(bucket);
        }
        return result;
    }

    /**
     * 返回缓存的克隆节点
     */
    public synchronized List<NodeTriadRouterNode> cloneCache() {
        return new ArrayList<>(nodeCache);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[myId=").append(myNode).append(", ");
        for (int i = 0; i < bucketSize; i++) {
            var bucket = buckets[i];
            sb.append("bucket").append(i).append("=[").append(bucket.toString()).append("], \r\n");
        }
        return sb.toString();
    }

    public int getPort() {
        return config.getNodePort();
    }

    public boolean isEmpty() {
        if (bucketSize > 1) {
            return false;
        }
        var bucket = buckets[0];
        return Objects.isNull(bucket) || bucket.isEmpty();
    }

    public int getBucketSize() {
        return bucketSize;
    }

    /**
     * 移除节点
     * @param node 要移除的路由节点
     */
    public void remove(NodeTriadRouterNode node) {
        int dis = EpidemicUtils.getDis(node.getKey(), myNode);
        dis = dis >= bucketSize ? bucketSize - 1 : dis;
        var bucket = buckets[dis];
        for (var n : bucket) {
            if (n.equals(node)) {
                bucket.remove(n);
                return;
            }
        }
    }

    /**
     * 移除缓存节点
     * @param node
     */
    public void removeCache(NodeTriadRouterNode node) {
        for (var n : nodeCache) {
            if (n.equals(node)) {
                nodeCache.remove(n);
                return;
            }
        }
    }

}
