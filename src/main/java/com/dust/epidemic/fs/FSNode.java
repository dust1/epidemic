package com.dust.epidemic.fs;

import io.vertx.core.Vertx;

/**
 * 本地文件系统节点
 * 对外对接DataNode
 */
public class FSNode {

    public static FSNode create(String dirPath) {
        //TODO Vertx的服务是采用SPI进行加载，这种好处就是将服务的接口与实现分离，
        // 两个通过META_INF文件下实现类全名进行加载
        // 通过本地文件的方式来指定接口的实现类
        return null;
    }

    /**
     * 根据文件在目录树中的关键字与文件即将写入的长度给他分配一个磁盘上唯一的uuid
     * @param virtualKey
     * @return
     */
    public String init(String virtualKey, long len) {
        return null;
    }

}
