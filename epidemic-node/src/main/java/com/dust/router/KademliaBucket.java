package com.dust.router;

import com.dust.fundation.LRUCache;

/**
 * Kademlia的路由桶，每个桶中维护一个由节点三元组组成的链表，这个链表的排序方式是
 */
public class KademliaBucket {

    /**
     * 成熟的LRU实现
     */
    private LRUCache<String, NodeTriadRouterNode> buckets;

    public KademliaBucket(int maxSize) {
        this.buckets = new LRUCache<>(maxSize);
    }


}
