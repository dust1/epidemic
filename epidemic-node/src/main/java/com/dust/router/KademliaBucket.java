package com.dust.router;

import com.dust.fundation.EpidemicUtils;
import com.dust.fundation.LRUCache;

import java.util.*;

/**
 * Kademlia的路由桶管理器，管理所有Bucket的生成/复制/分裂
 */
public class KademliaBucket {

    private int k;

    private List<NodeTriadRouterNode>[] buckets;

    private String myNode;

    private PriorityQueue<NodeTriadRouterNode> nodeCache;

    private int bucketSize;

    public KademliaBucket(int maxSize, String myNode) {
        this.k = maxSize;
        this.myNode = myNode;
        this.bucketSize = 1;

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
     * 往桶中添加一个路由表
     * @param node 要添加的路由信息
     */
    public void add(NodeTriadRouterNode node) {
        if (node.getKey().equals(myNode)) {
            return;
        }
        int prevIndex = EpidemicUtils.getDis(node.getKey(), myNode);
        var bucket = buckets[prevIndex];

        if (bucket.size() >= k) {
            //拆分桶。两种情况：1.存在后续桶。将新节点放入cache中等待垃圾回收进行重新排布；2.不存在后续桶。创建新桶
            if ((prevIndex + 1) < bucketSize) {
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
                var newBucket = new ArrayList<NodeTriadRouterNode>(k);
                List<NodeTriadRouterNode> oldBucket = new ArrayList<>(k);
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
            //不拆分桶，直接插入
            bucket.add(node);
        }
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

        //如果路由表中存在
        var bucket = buckets[index];
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

}
