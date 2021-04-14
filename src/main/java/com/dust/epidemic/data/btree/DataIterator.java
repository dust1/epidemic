package com.dust.epidemic.data.btree;

import java.util.*;

public class DataIterator implements Iterator<DataNode> {

    private LeafTreeNode leaf;
    private Queue<DataNode> queue;

    public DataIterator(LeafTreeNode leaf) {
        this.leaf = leaf;
        this.queue = new LinkedList<>();
    }

    @Override
    public boolean hasNext() {
        return Objects.nonNull(leaf) || !queue.isEmpty();
    }

    @Override
    public DataNode next() {
        if (queue.isEmpty()) {
            push();
        }
        return queue.remove();
    }

    private void push() {
        DataNode[] dataNodes = leaf.getDataNodes();
        for (int i = 0; i < leaf.size; i++) {
            queue.add(dataNodes[i]);
        }
        leaf = leaf.getNext();
    }
}
