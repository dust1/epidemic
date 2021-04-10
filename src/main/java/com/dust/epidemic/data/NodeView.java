package com.dust.epidemic.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 对节点自身数据的包装对象,用于网络间传输与操作
 */
public class NodeView<T, V extends Comparable<V>> extends BPlusTree<T, V> {

    public NodeView() {
        super();
    }

    public NodeView(int m) {
        super(m);
    }



}
