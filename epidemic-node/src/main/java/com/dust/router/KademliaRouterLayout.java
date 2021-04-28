package com.dust.router;

import com.dust.core.NodeConfig;

import java.io.IOException;
import java.util.List;

public class KademliaRouterLayout extends RouterLayout {

    public KademliaRouterLayout(NodeConfig config) {

    }

    @Override
    public void load() throws IOException {

    }

    @Override
    public List<NodeTriad> findNode(String key) {
        return null;
    }

    @Override
    public void addNode(NodeTriad newNode) {

    }

    @Override
    public FindValueResult findValue(String fileId) {
        return null;
    }
}
