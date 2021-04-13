package com.dust.epidemic.data;

/**
 * B树目录中数据的存储节点，用于维护数据本身以及相关描述
 */
public class DataNode {

    /**
     * 数据本身
     */
    private Object data;

    /**
     * 创建时间
     */
    private int createTime;

    /**
     * 修改时间
     */
    private int updateTime;

    public DataNode(Object data) {
        this.data = data;
        initTime();
    }

    private void initTime() {
        int timestamp = (int) System.currentTimeMillis() / 1000;
        this.createTime = timestamp;
        this.updateTime = timestamp;
    }

    public Object getData() {
        return data;
    }

    public String toString() {
        return data.toString();
    }

}
