package com.dust.router;


import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

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

    public int getUpdateTime() {
        return updateTime;
    }

    public void updateTime() {
        this.updateTime = (int) (System.currentTimeMillis() / 1000);
    }

    /**
     * 尝试从文件中读取一个网络存储节点
     * @param raf 文件通道
     * @return 如果能够读取到，则返回新建的对象；否则返回null
     */
    public static NodeTriadRouterNode fromFile(RandomAccessFile raf) throws IOException {
        //网络节点在文件中的存储信息结构如下
        //[头信息][节点id][节点host长度][节点host][节点port长度][节点port]

        int headIndex = 0;
        while (headIndex < NODE_HEAD.length && raf.getFilePointer() < raf.length()) {
            int n = raf.readInt();
            if (n == NODE_HEAD[headIndex]) {
                headIndex++;
            } else {
                headIndex = 0;
            }
        }
        if (headIndex < NODE_HEAD.length) {
            //找不到合格数据
            return null;
        }

        //nodeId固定长度40
        byte[] nodeBytes = new byte[40];
        raf.readFully(nodeBytes);
        String nodeId = new String(nodeBytes, StandardCharsets.UTF_8);

        int hostLen = raf.readInt();

        byte[] hostBytes = new byte[hostLen];
        raf.readFully(hostBytes);
        String host = new String(hostBytes, StandardCharsets.UTF_8);

        int port = raf.readInt();

        return new NodeTriadRouterNode(nodeId, host, port);
    }

    /**
     * 将网络节点三元组转化为持久化到本地的字节数据
     * @return 节点id、节点端口、节点host组成的字节数组信息
     */
    public ByteBuffer toBuffer() {
        int hostLen = getHost().length();
        int sumLen = NODE_HEAD.length * 4 + 40 + 4 + hostLen + 4;
        ByteBuffer resutl = ByteBuffer.allocate(sumLen);

        //16
        for (int head : NODE_HEAD) {
            resutl.putInt(head);
        }
        //40
        resutl.put(getKey().getBytes(StandardCharsets.UTF_8));
        //4
        resutl.putInt(hostLen);
        //n
        resutl.put(getHost().getBytes(StandardCharsets.UTF_8));
        //4
        resutl.putInt(getPort());
        return resutl;
    }



}
