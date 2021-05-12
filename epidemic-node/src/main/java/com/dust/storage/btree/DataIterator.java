package com.dust.storage.btree;

import java.util.*;

public class DataIterator implements Iterator<DataNode> {

    private LeafTreeNode leaf;
    private Queue<DataNode> queue;

    public DataIterator(LeafTreeNode leaf) {
        this.leaf = leaf;
        this.queue = new LinkedList<>();
        if (Objects.nonNull(leaf)) {
            push();
        }
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
        if (Objects.isNull(dataNodes)) {
            return;
        }
        queue.addAll(Arrays.asList(dataNodes).subList(0, leaf.size));
        leaf = leaf.getNext();
    }
}
