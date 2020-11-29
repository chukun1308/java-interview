package com.chukun.interview.lru;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chukun
 *  实现lru cache， 不借助linkedHashMap
 */
public class LruCache {

    // 定义内部节点
    static class Node<K,V> {
        K key;
        V value;
        Node<K,V> prev;
        Node<K,V> next;

        // 初始化Node
        public Node() {
            this.prev = null;
            this.next = null;
        }

        // 初始化Node
        public Node(K key,V value) {
            this.key = key;
            this.value = value;
            this.prev = null;
            this.next = null;
        }
    }

    // 定义一个双向链表
    static class DoubleLinkedList<K,V> {
        Node<K,V> head;
        Node<K,V> tail;

        public DoubleLinkedList() {
            this.head = new Node<>();
            this.tail = new Node<>();
            this.head.next = tail;
            this.tail.prev = head;
        }

        // 加入链表头部
        public void addHead(Node<K,V> node) {
            node.next = head.next;
            head.next.prev = node;
            node.prev = head;
            head.next = node;
        }

        // 移除节点
        public void removeNode(Node<K,V> node) {
            node.next.prev = node.prev;
            // 防止前一个是 head
            if (node.prev !=null) {
                node.prev.next = node.next;
            }else {
                head.next = node.next;
            }
            node.prev = null;
            node.next = null;
        }

        // 获取最后一个节点
        public Node<K,V> getLastNode() {
            return this.tail.prev;
        }
    }
    // 用于查找node
    private Map<Integer, Node<Integer,Integer>> map;
    // 用于存储node
    private DoubleLinkedList<Integer,Integer> doubleLinkedList;

    private int capacity;

    public LruCache(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>();
        this.doubleLinkedList = new DoubleLinkedList<>();
    }

    /**
     * get 操作
     * @param key
     * @return
     */
    public Integer get(Integer key) {
        if (!map.containsKey(key)) {
            return -1;
        }
        Node<Integer, Integer> node = map.get(key);
        // 先删除node本来的位置
        doubleLinkedList.removeNode(node);
        // 再加入到head节点
        doubleLinkedList.addHead(node);
        return node.value;
    }

    /**
     * put 操作
     * @param key
     * @param value
     */
    public void put(Integer key,Integer value) {
        if (map.containsKey(key)) {
            Node<Integer, Integer> node = map.get(key);
            node.value = value;
            //删除节点的原来位置
            doubleLinkedList.removeNode(node);
            doubleLinkedList.addHead(node);
        } else {
            // 达到容量上限，移除最后一个节点
            if (map.size() == capacity) {
                Node<Integer, Integer> lastNode = doubleLinkedList.getLastNode();
                map.remove(lastNode.key);
                doubleLinkedList.removeNode(lastNode);
            }
            Node<Integer, Integer> newNode = new Node<>(key, value);
            // 加入map中
            map.put(key, newNode);
            // 加入到链表中
            doubleLinkedList.addHead(newNode);
        }
    }
}
