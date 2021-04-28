package com.dust.router;

import java.io.IOException;
import java.util.List;

/**
 * 路由抽象类，除了Kademlia之外还可以用其他算法实现， 因此这里是一个通用接口
 * 通用的路由接口
 * 由于每种路由表的实现结构都不一样，因此这里不应该保存路由表的原始对象，而是应该记录路由表的通用参数以及对外接口
 */
public abstract class RouterLayout {

    /**
     * 加载路由快照文件并构建路由表
     * 网络路由表由Buckets组成
     * @throws IOException
     *      读取元数据文件失败
     */
    public abstract void load() throws IOException;

    /**
     * 根据给定节点id寻找{@link com.dust.core.NodeConfig}中设置的k个距离这个节点最近的节点三元组集合
     * 如果当前节点就是要寻找的节点则只返回一个节点
     * 但是不一定返回一个节点的都是节点匹配的情况。因为当当前节点只有一个节点的时候他也只能返回一个节点
     * @param key 要查询的节点id
     * @return
     *      距离这个节点最近的节点三元组信息集合
     */
    public abstract List<NodeTriad> findNode(String key);

    /**
     * 往路由表中新增一个网络节点
     * @param newNode 新增的节点信息三元组
     */
    public abstract void addNode(NodeTriad newNode);

    /**
     * 传入一个文件id，如果当前节点不存在该文件，则返回一个距离该文件所在服务器最近的K个节点信息三元组集合；
     * 如果存在则返回一个文件句柄，RPCs通过句柄读取文件内容。
     * @param fileId 传入的文件di
     * @return 如上所示
     */
    public abstract FindValueResult findValue(String fileId);


}
