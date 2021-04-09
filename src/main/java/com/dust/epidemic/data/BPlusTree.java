package com.dust.epidemic.data;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * B树入口
 */
public class BPlusTree<T, V extends Comparable<V>> {

    private Integer bTreeOrder;

    private Integer maxNumber;

    private Node<T, V> root;

    private LeafNode<T, V> left;

    public BPlusTree() {
        this(3);
    }

    public BPlusTree(Integer bTreeOrder) {
        this.bTreeOrder = bTreeOrder;
        this.maxNumber = bTreeOrder + 1;
        this.root = new LeafNode<>();
        this.left = null;
    }

    public void insert(V key, T value) {
        if (Objects.isNull(key)) {
            return;
        }

        Node<T, V> t = this.root.insert(key, value);
        if (t != null) {
            this.root = t;
        }
        this.left = (LeafNode<T, V>) this.root.refreshLeft();
    }

    public T find(V key) {
        T t = this.root.find(key);
        if (Objects.isNull(t)) {
            return null;
        }
        return t;
    }

    abstract class Node<T, V extends Comparable<V>> {
        protected Node<T, V> parent;
        protected Node<T, V>[] childs;
        protected Integer number;
        protected Object[] keys;

        public Node() {
            this.keys = new Object[maxNumber];
            this.childs = new Node[maxNumber];
            this.number = 0;
            this.parent = null;
        }

        abstract T find(V key);
        abstract Node<T, V> insert(V key, T value);
        abstract LeafNode<T, V> refreshLeft();

    }

    class BPlusNode<T, V extends Comparable<V>> extends Node<T, V> {
        public BPlusNode() {
            super();
        }

        @Override
        T find(V key) {
            int i = 0;
            while (i < this.number) {
                if (key.compareTo((V) this.keys[i]) <= 0) {
                    break;
                }
                i++;
            }
            if (this.number == i) {
                return null;
            }
            return this.childs[i].find(key);
        }

        @Override
        Node<T, V> insert(V key, T value) {
            int i = 0;
            while (i < this.number) {
                if (key.compareTo((V) this.keys[i]) < 0) {
                    break;
                }
                i++;
            }
            if (key.compareTo((V) this.keys[this.number - 1]) > 0) {
                i--;
            }

            return this.childs[i].insert(key, value);
        }

        @Override
        LeafNode<T, V> refreshLeft() {
            return this.childs[0].refreshLeft();
        }

        Node<T, V> insertNode(Node<T, V> node1, Node<T, V> node2, V key) {

            V oldKey = null;
            if (this.number > 0)
                oldKey = (V) this.keys[this.number - 1];

            if (key == null || this.number <= 0) {
                this.keys[0] = node1.keys[node1.number - 1];
                this.keys[1] = node2.keys[node2.number - 1];
                this.childs[0] = node1;
                this.childs[1] = node2;
                this.number += 2;
                return this;

            }
            int i = 0;
            while (key.compareTo((V) this.keys[i]) != 0) {
                i++;
            }
            this.keys[i] = node1.keys[node1.number - 1];
            this.childs[i] = node1;
            Object tempKeys[] = new Object[maxNumber];
            Object tempChilds[] = new Node[maxNumber];

            System.arraycopy(this.keys, 0, tempKeys, 0, i + 1);
            System.arraycopy(this.childs, 0, tempChilds, 0, i + 1);
            System.arraycopy(this.keys, i + 1, tempKeys, 0, this.number - i - 1);
            System.arraycopy(this.childs, i + 1, tempChilds, 0, this.number - i - 1);
            ;

            tempKeys[i + 1] = node2.keys[node2.number - 1];
            tempChilds[i + 1] = node2;
            this.number++;

            if (this.number <= bTreeOrder) {
                System.arraycopy(tempKeys, 0, this.keys, 0, this.number);
                System.arraycopy(tempChilds, 0, this.childs, 0, this.number);
                return null;
            }
            Integer middle = this.number / 2;
            BPlusNode<T, V> tempNode = new BPlusNode<T, V>();
            tempNode.number = this.number - middle;
            tempNode.parent = this.parent;
            if (this.parent == null) {
                BPlusNode<T, V> tempBPlusNode = new BPlusNode<>();
                tempNode.parent = tempBPlusNode;
                this.parent = tempBPlusNode;
                oldKey = null;
            }

            System.arraycopy(tempKeys, middle, tempNode.keys, 0, tempNode.number);
            System.arraycopy(tempChilds, middle, tempNode.childs, 0, tempNode.number);
            for (int j = 0; j < tempNode.number; j++) {
                tempNode.childs[j].parent = tempNode;

            }
            this.number = middle;
            this.keys = new Object[maxNumber];
            this.childs = new Node[maxNumber];
            System.arraycopy(tempKeys, 0, this.keys, 0, middle);
            System.arraycopy(tempChilds, 0, this.childs, 0, middle);

            BPlusNode<T, V> parentNode = (BPlusNode<T, V>) this.parent;
            return parentNode.insertNode(this, tempNode, oldKey);

        }

    }

    class LeafNode<T, V extends Comparable<V>> extends Node<T, V> {
        protected Object[] values;
        protected LeafNode<T, V> leftNode;
        protected LeafNode<T, V> rightNode;

        public LeafNode() {
            super();
            this.values = new Object[maxNumber];
            this.leftNode = null;
            this.rightNode = null;
        }

        @Override
        T find(V key) {
            if (this.number <= 0) {
                return null;
            }
            Integer left = 0;
            Integer right = this.number;
            Integer middle = (left + right) / 2;
            while (left < right) {
                V middleKey = (V) this.keys[middle];
                if (key.compareTo(middleKey) == 0) {
                    return (T) this.values[middle];
                } else if (key.compareTo(middleKey) < 0) {
                    right = middle;
                } else {
                    left = middle;
                }

                middle = (left + right) / 2;
            }
            return null;
        }

        @Override
        Node<T, V> insert(V key, T value) {
            V oldKey = null;
            if (this.number > 0)
                oldKey = (V) this.keys[this.number - 1];
            int i = 0;
            while (i < this.number) {
                if (key.compareTo((V) this.keys[i]) < 0)
                    break;
                i++;
            }

            Object tempKeys[] = new Object[maxNumber];
            Object tempValues[] = new Object[maxNumber];
            System.arraycopy(this.keys, 0, tempKeys, 0, i);
            System.arraycopy(this.values, 0, tempValues, 0, i);
            System.arraycopy(this.keys, i, tempKeys, i + 1, this.number - i);
            System.arraycopy(this.values, i, tempValues, i + 1, this.number - i);

            tempKeys[i] = key;
            tempValues[i] = value;
            this.number++;

            if (this.number <= bTreeOrder) {
                System.arraycopy(tempKeys, 0, this.keys, 0, this.number);
                System.arraycopy(tempValues, 0, this.values, 0, this.number);
                Node node = this;
                while (node.parent != null) {
                    V tempKey = (V) node.keys[node.number - 1];
                    if (tempKey.compareTo((V) node.parent.keys[node.parent.number - 1]) > 0) {
                        node.parent.keys[node.parent.number - 1] = tempKey;
                        node = node.parent;
                    }
                }
                return null;
            }
            Integer middle = this.number / 2;
            LeafNode<T, V> tempNode = new LeafNode<T, V>();
            tempNode.number = this.number - middle;
            tempNode.parent = this.parent;
            if (this.parent == null) {
                BPlusNode<T, V> tempBPlusNode = new BPlusNode<>();
                tempNode.parent = tempBPlusNode;
                this.parent = tempBPlusNode;
                oldKey = null;

            }
            System.arraycopy(tempKeys, middle, tempNode.keys, 0, tempNode.number);
            System.arraycopy(tempValues, middle, tempNode.values, 0, tempNode.number);

            this.number = middle;
            this.keys = new Object[maxNumber];
            this.values = new Object[maxNumber];
            System.arraycopy(tempKeys, 0, this.keys, 0, middle);
            System.arraycopy(tempValues, 0, this.values, 0, middle);

            this.rightNode = tempNode;
            tempNode.leftNode = this;

            BPlusNode<T, V> parentNode = (BPlusNode<T, V>) this.parent;

            return parentNode.insertNode(this, tempNode, oldKey);
        }

        @Override
        LeafNode<T, V> refreshLeft() {
            if (this.number <= 0) {
                return this;
            }
            return null;
        }
    }

}
