package com.dust.core;

import lombok.Getter;
import lombok.Setter;

import java.util.Properties;

/**
 * 节点配置对象
 * 信息包括：牵手节点、开启的端口、本地存储路径、节点的K值、
 */
@Getter
@Setter
public class NodeConfig {

    private String routerPath;

    private int port;

    private int bucketKey;

    private String nodeSalt;

    public NodeConfig(Properties properties) {

    }

}
