package com.dust.router;

import com.dust.fundation.LRUCache;

/**
 * Kademlia的路由桶管理器，管理所有Bucket的生成/复制/分裂
 */
public class KademliaBucket {

    /**
     * 成熟的LRU实现
     */
    private LRUCache<String, NodeTriadRouterNode> buckets;

    public KademliaBucket(int maxSize) {
        this.buckets = new LRUCache<>(maxSize);
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

    }


}
