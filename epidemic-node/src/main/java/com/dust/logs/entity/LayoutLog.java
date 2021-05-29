package com.dust.logs.entity;

import com.dust.logs.LogFormat;
import com.dust.router.kademlia.NodeTriadRouterNode;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

/**
 * 路由节点日志
 */
@Getter
@ToString
public class LayoutLog {

    enum Type {
        ADD("Bucket_Add"),
        CACHE_ADD("Cache_Add");

        private String key;

        Type(String key) {
            this.key = key;
        }

        public static Type byKey(String key) {
            for (var type : values()) {
                if (type.key.equals(key)) {
                    return type;
                }
            }
            return null;
        }

    }

    private Type type;

    private String host;

    private int port;

    private String nodeId;

    public static LayoutLog parser(String log) {
        if (Objects.isNull(log) || log.isBlank()) {
            return null;
        }
        if (log.contains(LogFormat.CHECK_POINT_FORMAT)) {
            return null;
        }

        int typeStart = log.lastIndexOf("[");
        int typeEnd = log.lastIndexOf("]");
        String typeStr = log.substring(typeStart + 1, typeEnd);
        var type = Type.byKey(typeStr);
        int index = log.lastIndexOf("-");
        String infoStr = log.substring(index + 2);
        String[] infoList = infoStr.split(",");

        String hostStr = infoList[0];
        int hostIndex = hostStr.indexOf(":");
        String host = hostStr.substring(hostIndex + 1);

        String portStr = infoList[1];
        int port = Integer.parseInt(portStr.split(":")[1]);

        String nodeIdStr = infoList[2];
        String nodeId = nodeIdStr.split(":")[1];

        return new LayoutLog(type, host, port, nodeId);
    }

    private LayoutLog(Type type, String host, int port, String nodeId) {
        this.type = type;
        this.host = host;
        this.port = port;
        this.nodeId = nodeId;
    }

    public NodeTriadRouterNode toNode() {
        return new NodeTriadRouterNode(nodeId, host, port);
    }

}
