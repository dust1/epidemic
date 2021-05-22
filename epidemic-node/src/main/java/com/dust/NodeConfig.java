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
            Parameter.ORDER_NUM,
            Parameter.LAYOUT_SAVE_MAX_SIZE,
            Parameter.LAYOUT_SAVE_MAX_TIME,
            Parameter.LAYOUT_START_PING_COUNT,
            Parameter.CONTACT_HOST,
            Parameter.CONTACT_PORT
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

    public int getLayoutSaveMaxSize() {
        return (Integer) parameter.get(Parameter.LAYOUT_SAVE_MAX_SIZE);
    }

    public int getLayoutSaveMaxTime() {
        return (Integer) parameter.get(Parameter.LAYOUT_SAVE_MAX_TIME);
    }

    public int getStartPingCount() {
        return (Integer) parameter.get(Parameter.LAYOUT_START_PING_COUNT);
    }

    public String getContactHost() {
        return (String) parameter.get(Parameter.CONTACT_HOST);
    }

    public int getContactPort() {
        return (Integer) parameter.get(Parameter.CONTACT_PORT);
    }
}
