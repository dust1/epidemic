package com.dust.router.kademlia;


import com.dust.fundation.EpidemicUtils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 存储在Bucket中的网络节点路由表
 */
public class NodeTriadRouterNode extends NodeTriad {

    /**
     * 节点数据写入过程中的头信息
     */
    private static final byte[] NODE_HEAD = {0xc, 0xa, 0xf, 0xe, 0xb, 0xa, 0xb, 0xe};

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
        //[头信息][节点id][节点host长度][节点host][节点port]

        if (!EpidemicUtils.checkHead(NODE_HEAD, raf)) {
            return null;
        }

        //nodeId固定长度40
        String nodeId = EpidemicUtils.readToSHA1(raf);

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
        int sumLen = NODE_HEAD.length + 40 + 4 + hostLen + 4;
        ByteBuffer resutl = ByteBuffer.allocate(sumLen);

        //8
        resutl.put(NODE_HEAD);

        //40
        resutl.put(getKey().getBytes(StandardCharsets.UTF_8));
        //4
        resutl.putInt(hostLen);
        //n
        resutl.put(getHost().getBytes(StandardCharsets.UTF_8));
        //4
        resutl.putInt(getPort());

        resutl.flip();
        return resutl;
    }

    @Override
    public String toString() {
        return String.format("[nodeId=%s, host=%s, port=%d, updateTime=%d]",
                getKey(), getHost(), getPort(), updateTime);
    }

    @Override
    public boolean equals(Object obj) {
        if (Objects.isNull(obj)) {
            return false;
        }
        if (!(obj instanceof NodeTriadRouterNode)) {
            return false;
        }
        var source = (NodeTriadRouterNode) obj;
        return source.getKey().equals(getKey())
                && source.getHost().equals(getHost())
                && source.getPort() == getPort();
    }

}
