package com.dust.router;

import com.dust.fundation.EpidemicUtils;
import com.dust.fundation.LRUCache;

import java.util.*;

/**
 * Kademlia的路由桶管理器，管理所有Bucket的生成/复制/分裂
 */
public class KademliaBucket {

    private int k;

    private List[] buckets;

    private String myNode;

    private PriorityQueue<NodeTriadRouterNode> nodeCache;

    private int bucketSize;

    public KademliaBucket(int maxSize, String myNode) {
        this.buckets = new ArrayList[160];
        this.k = maxSize;
        this.bucketSize = 1;
        this.myNode = myNode;
        this.nodeCache = new PriorityQueue<>(k, (n1, n2) -> Integer.compare(n2.getUpdateTime(), n1.getUpdateTime()));

        this.buckets[0] = new ArrayList<NodeTriadRouterNode>(k);
    }

    /**
     * 清空桶
     * 将桶中的所有节点数据全部清除
     */
    public void clear() {

    }

    /**
     * 往桶中添加一个路由表
     * @param node
     */
    public void add(NodeTriadRouterNode node) {
        int prevIndex = EpidemicUtils.getDis(node.getKey(), myNode);
        if (prevIndex >= buckets.length) {
            prevIndex = buckets.length - 1;
        }
        List<NodeTriadRouterNode> bucket = buckets[prevIndex];

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
                }
            }
        } else {
            //不拆分桶，直接插入
            bucket.add(node);
        }
    }


}
