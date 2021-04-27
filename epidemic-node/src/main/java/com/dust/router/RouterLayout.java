package com.dust.router;

import java.io.IOException;

/**
 * 路由抽象类，除了Kademlia之外还可以用其他算法实现， 因此这里是一个通用接口
 */
public abstract class RouterLayout {

    /**
     * 加载路由快照文件并构建路由表
     * 网络路由表由Buckets组成
     * @throws IOException
     *      读取元数据文件失败
     */
    public abstract void load() throws IOException;

}
