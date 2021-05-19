package com.dust.router;

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
public abstract class RouterLayout {

    /**
     * 网络路由模块的版本名称
     */
    public static final String VERSION_FILENAME = ".router_version";

    /**
     * 网络路由模块的快照文件名称
     */
    public static final String SNAPSHOT_FILENAME = "node.cache";

    /**
     * 路由信息持久化保存的文件夹
     */
    protected final String routerPath;


    protected NodeConfig config;

    protected RouterLayout(NodeConfig config) throws IOException {
        this.config = config;
        String tmp = config.getRouterPath();
        if (!tmp.endsWith("/")) {
            tmp += "/";
        }
        //检查版本信息
        EpidemicUtils.checkAndWriteVersion(tmp,
                VERSION_FILENAME, this::isCompatibleVersion, getVersion());
        this.routerPath = tmp;
    }

    /**
     * 加载路由快照文件并构建路由表
     * 网络路由表由Buckets组成
     * @throws IOException
     *      读取元数据文件失败
     */
    public abstract void load() throws IOException;

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
     * 检查当前的实现版本与给定的版本是否兼容
     * @param version 检查的版本
     * @return 如果兼容则返回true，否则返回false
     */
    protected abstract boolean isCompatibleVersion(long version);

    /**
     * 获取当前系统的实现版本
     * @return 当前系统版本的string形式
     */
    protected abstract long getVersion();

    /**
     * 持久化方法，如果实现类需要将数据持久化的化需要继承并实现
     * @throws IOException
     *         写入本地磁盘失败
     */
    protected void persistence() throws IOException {
        //...
    }

    /**
     * 获取当前节点id信息
     */
    public abstract String getMyId();

    /**
     * 某个节点ping到当前节点
     */
    public abstract void ping(String nodeId, String host, int port);

    /**
     * 尝试ping氢气
     * @param info 请求的节点信息
     * @param host 请求的网络地址
     */
    public void ping(NodeInfo info, String host) {
        ping(info.getNodeId(), host, info.getPort());
    }

}
