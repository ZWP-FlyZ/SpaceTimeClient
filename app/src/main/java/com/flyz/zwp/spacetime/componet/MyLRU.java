package com.flyz.zwp.spacetime.componet;

/**
 * Created by zwp12 on 2017/3/14.
 */

import java.util.HashMap;
import java.util.Map;

public class MyLRU<K, V> {
    private int maxSize;
    private Map<K, DoublyLinkedNode<K, V>> store;
    private DoublyLinkedList<K,V> sequence;

    public MyLRU(int maxSize) {
        this.maxSize = maxSize;
        this.store = new HashMap<>(maxSize);
        this.sequence = new DoublyLinkedList<>();
    }

    public void add(K key, V value) {
        if (!store.containsKey(key)) {
            DoublyLinkedNode<K, V> valueNode = new DoublyLinkedNode<>(key, value);
            valueNode = sequence.addInFront(valueNode);
            store.put(key, valueNode);

            if (store.size() > maxSize) {
                DoublyLinkedNode<K, V> temp = sequence.removeLast();
                store.remove(temp.key);
            }
        } else {
            sequence.bringToFront(store.get(key));
        }
    }

    public V get(K key) {
        if (!store.containsKey(key))
            return null;

        DoublyLinkedNode<K, V> node = store.get(key);
        sequence.bringToFront(node);
        return node.value;
    }

    public void printAll() {
        sequence.printList();
        System.out.println(store);
    }

    public boolean contain(K key){
        return store.containsKey(key);
    }

    class DoublyLinkedNode<T1, T2> {
        T1 key;
        T2 value;
        DoublyLinkedNode<T1, T2> left;
        DoublyLinkedNode<T1, T2> right;

        public DoublyLinkedNode(T1 key, T2 value) {
            this.key = key;
            this.value = value;
        }

        public String toString() {
            String leftStr = (left != null) ? String.valueOf(left.key) + ", " + String.valueOf(left.value) : "null";
            String rightStr = (right != null) ? String.valueOf(right.key) + ", " + String.valueOf(right.value) : "null";

            return "| (" + leftStr + ") <- (" + key + ", " + value + ") -> (" + rightStr + ") |";
        }
    }

    class DoublyLinkedList<T3, T4> {
        private DoublyLinkedNode<T3, T4> head;
        private DoublyLinkedNode<T3, T4> tail;

        public DoublyLinkedNode<T3, T4> addInFront(DoublyLinkedNode<T3, T4> node) {
            if (head == null) {
                // tail is null too
                head = node;
                tail = node;
            } else {
                node.right = head;
                head.left = node;
                head = node;
            }
            return node;
        }

        public DoublyLinkedNode<T3, T4> removeLast() {
            DoublyLinkedNode<T3, T4> temp = tail;
            if (tail == null)
                return null;
            else if (tail == head) {
                tail = head = null;
            } else {
                tail = tail.left;
                tail.right = null;
            }
            return temp;
        }

        public DoublyLinkedNode<T3, T4> bringToFront(DoublyLinkedNode<T3, T4> node) {
            System.out.println("Bringing " + node.value + " to front.");
            if (head == node) {
                return head;
            } else if (tail == node) {
                tail = tail.left;
                tail.right = null;
            } else {
                DoublyLinkedNode<T3, T4> prev = node.left;
                DoublyLinkedNode<T3, T4> next = node.right;
                prev.right = next;
                next.left = prev;
            }
            node.right = head;
            head.left = node;
            node.left = null;
            head = node;
            return head;
        }

        public void printList() {
            DoublyLinkedNode<T3, T4> temp = head;
            while (temp != null) {
                System.out.print(temp + " -> ");
                temp = temp.right;
            }
            System.out.println("/");
        }
    }
}
