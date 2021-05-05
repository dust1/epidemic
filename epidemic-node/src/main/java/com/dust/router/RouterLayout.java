package com.dust.router;

import com.dust.core.NodeConfig;
import com.dust.fundation.EpidemicUtils;

import java.io.IOException;
import java.io.RandomAccessFile;
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
    public static final String VERSION_FILENAME = ".routerVersion";

    /**
     * 网络路由模块的快照文件名称
     */
    public static final String SNAPSHOT_FILENAME = "node.cache";

    /**
     * 路由信息持久化保存的文件夹
     */
    protected final String routerPath;

    /**
     * 对快照文件进行随机读写的对象
     * 默认不会初始化
     */
    protected RandomAccessFile snapshot = null;

    protected NodeConfig config;

    protected RouterLayout(NodeConfig config) throws IOException {
        this.config = config;
        String tmp = config.getRouterPath();
        if (!tmp.endsWith("/")) {
            tmp += "/";
        }
        //检查版本信息
        EpidemicUtils.checkAndwriteVersion(tmp,
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

    /**
     * 检查当前的实现版本与给定的版本是否兼容
     * @param version 检查的版本
     * @return 如果兼容则返回true，否则返回false
     */
    protected abstract boolean isCompatibleVersion(int version);

    /**
     * 获取当前系统的实现版本
     * @return 当前系统版本的string形式
     */
    protected abstract String getVersion();

    /**
     * 获取持久化到本地的一个网络节点的大小
     * 如果不需要持久化则不用重载该方法
     * @return 如果不用持久化，则返回-1
     */
    protected int getPersistenceNodeSize() {
        return -1;
    }

    /**
     * 持久化方法，如果实现类需要将数据持久化的化需要继承并实现
     * @throws IOException
     *         写入本地磁盘失败
     */
    protected void persistence() throws IOException {
        //...
    }

}
