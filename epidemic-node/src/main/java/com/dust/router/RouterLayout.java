package com.dust.router;

import com.dust.BaseLayout;
import com.dust.NodeConfig;
import com.dust.fundation.EpidemicUtils;
import com.dust.grpc.kademlia.NodeInfo;
import com.dust.router.kademlia.NodeTriad;

import java.io.IOException;
import java.util.List;

/**
 * 路由抽象类，除了Kademlia之外还可以用其他算法实现， 因此这里是一个通用接口
 * 通用的路由接口
 * 由于每种路由表的实现结构都不一样，因此这里不应该保存路由表的原始对象，而是应该记录路由表的通用参数以及对外接口
 */
public abstract class RouterLayout extends BaseLayout {

    protected RouterLayout(String path, NodeConfig config,
                           long version, String versionFileName, byte[] head) throws IOException {
        super(path, version, versionFileName, head, config);
    }

    /**
     * 根据给定节点id寻找{@link NodeConfig}中设置的k个距离这个节点最近的节点三元组集合
     * 如果当前节点就是要寻找的节点则只返回一个节点
     * 但是不一定返回一个节点的都是节点匹配的情况。因为当当前节点只有一个节点的时候他也只能返回一个节点
     * @param key 要查询的节点id
     * @return
     *      距离这个节点最近的节点三元组信息集合
     */
    public abstract List<NodeTriad> findNode(String key);

    /**
     * 获取当前节点id信息
     */
    public abstract String getMyId();

}
