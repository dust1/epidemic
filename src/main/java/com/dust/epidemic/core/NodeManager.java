package com.dust.epidemic.core;

import com.dust.epidemic.net.Descriptor;
import com.dust.epidemic.net.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 集群的节点管理器
 */
public class NodeManager {

    private List<Node> nodes;

    private String host;

    private int port;

    public NodeManager(String host, int port) {
        this.host = host;
        this.port = port;
        this.nodes = new ArrayList<>();
    }

    /**
     * 随机获取一部分Node用于转发
     * @return
     */
    public List<Node> selectPeer(Descriptor sourceAddress, Descriptor address) {
        return nodes.stream().filter(n -> !n.getAddress().equals(address.getAddress()) && !n.getAddress().equals(sourceAddress.getAddress()))
                .collect(Collectors.toList());
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    /**
     * 获取当前节点描述符
     * @return
     */
    public Descriptor getMyDescriptor() {
        Descriptor descriptor = new Descriptor();
        descriptor.setAddress(host);
        descriptor.setPort(port);
        descriptor.setHotCount(0);
        return descriptor;
    }

}
