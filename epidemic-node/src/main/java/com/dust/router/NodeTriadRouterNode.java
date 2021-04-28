package com.dust.router;

/**
 * 存储在Bucket中的网络节点路由表
 */
public class NodeTriadRouterNode extends NodeTriad {

    /**
     * 更新时间
     * 根据更新时间排列，最近最少使用的放后面
     */
    private int updateTime;

}
