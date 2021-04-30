package com.dust.router;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;

/**
 * 存储在Bucket中的网络节点路由表
 */
public class NodeTriadRouterNode extends NodeTriad {

    /**
     * 节点数据写入过程中的头信息
     */
    private static final int[] NODE_HEAD = {0xca, 0xfe, 0xba, 0xbe};

    /**
     * 更新时间
     * 根据更新时间排列，最近最少使用的放后面
     */
    private int updateTime;

    public NodeTriadRouterNode(String nodeId, String host, int port) {
        super(nodeId, host, port);
        this.updateTime = (int) (System.currentTimeMillis() / 1000);
    }

    /**
     * 尝试从文件中读取一个网络存储节点
     * @param channel 文件通道
     * @return 如果能够读取到，则返回新建的对象；否则返回null
     */
    public static NodeTriadRouterNode fromFile(FileChannel channel) throws IOException {
        //网络节点在文件中的存储信息结构如下
        //[头信息][节点id][节点host长度][节点host][节点port长度][节点port]

        ByteBuffer headBuffer = ByteBuffer.allocate(2);
        int headIndex = 0;
        while (headIndex < NODE_HEAD.length) {
            int len = channel.read(headBuffer);
            if (len == -1) {
                return null;
            }
            headBuffer.flip();
            int num = headBuffer.asIntBuffer().get();
            if (num == NODE_HEAD[headIndex]) {
                headIndex++;
            } else {
                headIndex = 0;
            }
            headBuffer.clear();
        }

        //TODO 可以将这个步骤抽象一下。毕竟很多代码相同
        ByteBuffer idBuffer = ByteBuffer.allocate(40);
        channel.read(idBuffer);
        idBuffer.flip();
        String nodeId = idBuffer.toString();

        ByteBuffer hostLenBuffer = ByteBuffer.allocate(1);
        channel.read(hostLenBuffer);
        hostLenBuffer.flip();
        int hostLen = hostLenBuffer.asIntBuffer().get();

        ByteBuffer hostBuffer = ByteBuffer.allocate(hostLen);
        channel.read(hostBuffer);
        hostBuffer.flip();
        String host = hostBuffer.toString();

        ByteBuffer portLenBuffer = ByteBuffer.allocate(1);
        channel.read(portLenBuffer);
        portLenBuffer.flip();
        int portLen = portLenBuffer.asIntBuffer().get();

        ByteBuffer portBuffer = ByteBuffer.allocate(portLen);
        channel.read(portBuffer);
        portBuffer.flip();
        int port = portBuffer.asIntBuffer().get();

        return new NodeTriadRouterNode(nodeId, host, port);
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
