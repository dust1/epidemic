package com.dust.router.entity;

import com.dust.router.kademlia.NodeTriad;
import com.dust.storage.btree.DataNode;

import java.util.List;

/**
 * findValue返回结果
 * 有两种可能：1.存在文件， 则其中的文件句柄不为空
 *          2。 不存在文件，则其中的三元组集合对象不为空
 */
public class FindValueResult {

    //这个返回对象的模式：1存在文件、2不存在文件
    private final int mode;

    private List<NodeTriad> nodeTriads;

    /**
     * 根据文件创建返回结果
     * 返回结果是存在文件句柄
     */
    public static FindValueResult byFile(DataNode dataNode) {
        return new FindValueResult(dataNode);
    }

    /**
     * 根据节点创建返回结果
     * 返回结果是节点三元组集合
     */
    public static FindValueResult byNode(List<NodeTriad> nodeTriads) {
        return new FindValueResult(nodeTriads);
    }

    private FindValueResult(DataNode dataNode) {
        this.mode = 1;
        //TODO 根据DataNode创建文件句柄
    }

    private FindValueResult(List<NodeTriad> nodeTriads) {
        this.mode = 2;
        this.nodeTriads = nodeTriads;
    }

}
