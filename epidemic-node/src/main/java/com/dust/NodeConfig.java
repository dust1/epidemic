package com.dust;

import java.util.Properties;

/**
 * 节点配置对象
 * 信息包括：牵手节点、开启的端口、本地存储路径、节点的K值、
 */
public class NodeConfig extends Config {

    private final Parameter[] nodeParameter = {
            Parameter.ROUTER_PATH,
            Parameter.STORAGE_PATH,
            Parameter.NODE_PORT,
            Parameter.BUCKET_KEY,
            Parameter.NODE_SALT,
            Parameter.CHUNK_SIZE,
            Parameter.ORDER_NUM
    };

    public NodeConfig(Properties properties) {
        super(properties);

        read();
    }

    private void read() {
        for (var param : nodeParameter) {
            parameter.put(param, readParameter(param));
        }
    }

    public String getRouterPath() {
        return (String) parameter.get(Parameter.ROUTER_PATH);
    }

    public String getStoragePath() {
        return (String) parameter.get(Parameter.STORAGE_PATH);
    }

    public int getNodePort() {
        return (Integer) parameter.get(Parameter.NODE_PORT);
    }

    public int getBucketKey() {
        return (Integer) parameter.get(Parameter.BUCKET_KEY);
    }

    public String getNodeSalt() {
        return (String) parameter.get(Parameter.NODE_SALT);
    }

    public String getChunkSize() {
        return (String) parameter.get(Parameter.CHUNK_SIZE);
    }

    public int getOrderNum() {
        return (Integer) parameter.get(Parameter.ORDER_NUM);
    }

}
