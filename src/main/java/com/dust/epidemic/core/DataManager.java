package com.dust.epidemic.core;

/**
 * 节点中对所有存储信息的管理表
 */
public class DataManager {

    private NodeView view;

    private String savePath;

    public DataManager(String savePath) {
        this.savePath = savePath;
    }

    /**
     * 将另一个节点的数据跟自身数据合并
     */
    public void merge(NodeView newView) {

    }

}
