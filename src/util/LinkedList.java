package util;

import java.io.Serializable;
import java.util.Iterator;
import java.util.function.Predicate;

public class LinkedList<T> implements Iterable<T>, Serializable {
    protected Node head;
    protected Node tail;
    protected int size;

    public LinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public void addLast(T value) {
        Node newNode = new Node(value);
        if (head == null) {
            head = newNode;
            tail = newNode;
            this.size++;
            return;
        }
        this.tail.next = newNode;
        this.size++;
    }

    public void addFirst(T value) {
        Node newNode = new Node(value);
        if (this.head == null) {
            this.head = newNode;
            this.tail = newNode;
            this.size++;
            return;
        }
        newNode.next = this.head;
        this.head = newNode;
        this.size++;
    }

    public void remove(int i) {
        if (i == 0) {
            this.head = this.head.next;
            if (this.head == null) {
                this.tail = null;
            }
            return;
        }
        Node temp = this.head;
        while (i-- > 1) {
            temp = temp.next;
        }
        temp.next = temp.next.next;
        this.size--;
    }

    public boolean exists(T value) {
        Node c = this.head;
        while (c != null) {
            if (c.data.equals(value)) {
                return true;
            }
            c = c.next;
        }
        return false;
    }

    public int size() {
        return this.size;
    }

    public T get(int i) {
        Node c = this.head;
        try {
            while (i-- > 0) {
                c = c.next;
            }
        } catch (NullPointerException e) {
            throw new IndexOutOfBoundsException();
        }
        return c.data;
    }

    @Override
    public Iterator<T> iterator() {
        return new ListIterator();
    }

    public void remove(T castle) {
        Node temp = this.head;
        try {
            while (temp != null) {
                if (temp.next.data.equals(castle)) {
                    temp.next = temp.next.next;
                    return;
                }
                temp = temp.next;
            }
        } catch (NullPointerException ignored) {}
    }

    public boolean isEmpty() {
        return this.head == null;
    }

    public void removeIf(Predicate<? super T> filter) {
        while(this.head==null) {
            if (filter.test(this.head.data)) {
                this.head = this.head.next;
            } else {
                break;
            }
        }
        Node temp = this.head;
        while (temp.next != null) {
            if (filter.test(temp.next.data)) {
                temp.next = temp.next.next;
            }
            temp = temp.next;
        }
    }

    private class Node implements Serializable {
        public T data;
        public Node next;

        public Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    class ListIterator implements Iterator<T> {
        Node current;

        public ListIterator() {
            this.current = head;
        }

        public boolean hasNext() {
            return current != null;
        }

        public T next() {
            T data = current.data;
            current = current.next;
            return data;
        }
    }
}
