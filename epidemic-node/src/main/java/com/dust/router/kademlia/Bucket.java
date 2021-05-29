package com.dust.router.kademlia;

import java.util.*;

/**
 * 单个Bucket对象
 */
public class Bucket {

    private final Queue<NodeTriadRouterNode> queue;

    public Bucket(int bucketKey) {
        this.queue = new PriorityQueue<>(bucketKey, (n1, n2) -> Integer.compare(n2.getUpdateTime(), n1.getUpdateTime()));
    }

    /**
     * 检查这个下标Bucket中是否有对应id的节点
     * @param nodeId 是否存在的节点id
     */
    public boolean contains(String nodeId) {
        for (var node : queue) {
            if (node.getKey().equals(nodeId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取这个Bucket队列中的节点元素数量
     */
    public int size() {
        return queue.size();
    }

    /**
     * 往Bucket队列中添加元素
     */
    public synchronized void add(NodeTriadRouterNode node) {
        node.updateTime();
        queue.add(node);
    }

    /**
     * 移除某个节点id对应的节点
     * @return 如果Bucket中存在该节点并且移除成功，则返回true。否则返回false
     */
    public synchronized boolean remove(String nodeId) {
        for (var node : queue) {
            if (node.getKey().equals(nodeId)) {
                queue.remove(node);
                return true;
            }
        }
        return false;
    }

    /**
     * 刷新队列中节点的updateTime
     * 这个操作会更新队列顺序，将最新的节点放到队列最前
     * @param nodeId 节点id
     */
    public synchronized void refreshNode(String nodeId) {
        NodeTriadRouterNode refreshNode = null;
        for (var node : queue) {
            if (node.getKey().equals(nodeId)) {
                refreshNode = node;
                break;
            }
        }
        if (Objects.isNull(refreshNode)) {
            return;
        }
        queue.remove(refreshNode);
        refreshNode.updateTime();
        queue.add(refreshNode);
    }

    /**
     * 返回一份Bucket存储节点的复制
     */
    public Collection<NodeTriadRouterNode> cloneNode() {
        return new ArrayList<>(queue);
    }

}
