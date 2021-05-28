package com.dust;

import com.dust.logs.LogFormat;
import com.dust.logs.Logger;

import java.util.Objects;
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
            Parameter.CONTACT_HOST,
            Parameter.RE_PUBLISHING_TIME,
            Parameter.RE_PUBLISHING_THREAD_POOL_SIZE,
            Parameter.CONTACT_PORT,
            Parameter.ROUTER_SAVE_COUNT
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

    public long getChunkSize() {
        var size = (String) parameter.get(Parameter.CHUNK_SIZE);
        if (Objects.isNull(size) || size.isBlank()) {
            //未配置参数
            return 0;
        }
        if (size.length() <= 2) {
            //参数配置错误
            return -1;
        }
        size = size.toUpperCase();
        String tmp = size.substring(0, size.length() - 2);;
        if (size.endsWith("KB")) {
            return Integer.parseInt(tmp) * 1024L;
        } else if (size.endsWith("MB")) {
            return Integer.parseInt(tmp) * 1024L * 1024L;
        } else if (size.endsWith("GB")) {
            return Integer.parseInt(tmp) * 1024L * 1024L * 1024L;
        }
        return -1;
    }

    public int getOrderNum() {
        return (Integer) parameter.get(Parameter.ORDER_NUM);
    }

    public long getRePublishingTime() {
        String time = (String) parameter.get(Parameter.RE_PUBLISHING_TIME);
        long defaultTime = 60 * 60;     //默认1小时
        if (time.length() < 2) {
            Logger.systemLog.warn(LogFormat.SYSTEM_INFO_FORMAT, "RE_PUBLISHING_TIME参数错误：" + time + "，采用默认参数：1h");
            return defaultTime;
        }
        String numStr = time.substring(0, time.length() - 1);

        if (time.endsWith("d")) {
            try {
                long day = Integer.parseInt(numStr);
                return day * 24 * 60 * 60;
            } catch (NumberFormatException e) {
                Logger.systemLog.warn(LogFormat.SYSTEM_INFO_FORMAT, "RE_PUBLISHING_TIME参数错误：" + time + "，采用默认参数：1h");
                return defaultTime;
            }
        } else if (time.endsWith("h")) {
            try {
                long hour = Integer.parseInt(numStr);
                return hour * 60 * 60;
            } catch (NumberFormatException e) {
                Logger.systemLog.warn(LogFormat.SYSTEM_INFO_FORMAT, "RE_PUBLISHING_TIME参数错误：" + time + "，采用默认参数：1h");
                return defaultTime;
            }
        } else {
            Logger.systemLog.warn(LogFormat.SYSTEM_INFO_FORMAT, "RE_PUBLISHING_TIME参数错误：" + time + "，采用默认参数：1h");
            return defaultTime;
        }
    }

    public String getContactHost() {
        return (String) parameter.get(Parameter.CONTACT_HOST);
    }

    public int getContactPort() {
        return (Integer) parameter.get(Parameter.CONTACT_PORT);
    }

    public int getRePublishingThreadPoolSize() {
        return (Integer) parameter.get(Parameter.RE_PUBLISHING_THREAD_POOL_SIZE);
    }

    public int getRouterSaveCount() {
        return (Integer) parameter.get(Parameter.ROUTER_SAVE_COUNT);
    }

}
