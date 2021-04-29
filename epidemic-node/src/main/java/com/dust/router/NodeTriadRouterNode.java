package com.dust.router;

import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.nio.channels.FileChannel;

/**
 * 存储在Bucket中的网络节点路由表
 */
public class NodeTriadRouterNode extends NodeTriad {

    /**
     * 更新时间
     * 根据更新时间排列，最近最少使用的放后面
     */
    private int updateTime;

    /**
     * 尝试从文件中读取一个网络存储节点
     * @param channel 文件通道
     * @return 如果能够读取到，则返回新建的对象；否则返回null
     */
    public static NodeTriadRouterNode fromFile(FileChannel channel) {
        //网络节点在文件中的存储信息结构如下
        //TODO
        return null;
    }

    /**
     * 将网络节点三元组转化为持久化到本地的字节数据
     * @return 节点id、节点端口、节点host组成的字节数组信息
     */
    public static Buffer toFile() {
        //TODO
        return null;
    }

}
