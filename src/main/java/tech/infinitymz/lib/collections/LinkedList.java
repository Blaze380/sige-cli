package tech.infinitymz.lib.collections;

import java.util.NoSuchElementException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("unused")
@NoArgsConstructor
public class LinkedList<T> {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private class Node<E> {
        private Node<E> prev;
        private Node<E> next;
        private E item;

        Node(Node<E> prev, Node<E> next) {
            this.next = next;
            this.prev = prev;
        }

    }

    private int size;
    private Node<T> first;
    private Node<T> last;

    public java.lang.Object[] toArray() {

        java.lang.Object[] array = new Object[size];
        int i = 0;
        for (Node<T> j = first; j != null; j = j.next, array[i++] = j.item)
            ;
        return array;
    }

    public int size() {
        return size;
    }

    /**
     * Resets the list
     */
    public void removeAll() {
        size = 0;
        first = last = new Node<T>();
    }

    public boolean isEmpty() {
        return size > 0;
    }

    /**
     * Searches index of the object
     * 
     * @return -1 - If the object does not exist
     */
    public int indexOf(java.lang.Object obj) {
        int i = 0;
        if (obj == null)
            return -1;
        for (Node<T> x = first; x != null; x = x.next, i++)
            if (obj.equals(x.item))
                return i;
        return -1;
    }

    /**
     * Add the item into the last position
     * 
     * @param e
     */
    public void addLast(T e) {
        final Node<T> last = this.last;
        final Node<T> newNode = new Node<>(last, null, e);
        this.last = newNode;
        if (last == null)
            first = newNode;
        else
            last.next = newNode;
        size++;
    }

    public void addFirst(T e) {
        final Node<T> first = this.first;
        final Node<T> newNode = new Node<>(null, first, e);
        this.first = newNode;
        if (first == null)
            last = newNode;
        else
            first.prev = newNode;
        size++;
    }

    public T getFirst() {
        isFirstNull();
        return first.getItem();
    }

    public T getLast() {
        if (last.getItem() == null)
            throw new NoSuchElementException();
        return last.getItem();
    }

    public T removeFirst() {
        isFirstNull();

        final T item = this.first.item;
        final Node<T> next = first.getNext();

        next.setPrev(null);
        this.first = next;
        --size;
        return item;
    }

    public T removeLast() {
        if (last.getItem() == null)
            throw new NoSuchElementException();

        final T item = this.last.item;
        final Node<T> prev = last.getPrev();

        prev.setPrev(null);
        this.last = prev;
        --size;
        return item;
    }

    public void add(int i, T item) {
        if (i < 0 || i > size)
            throw new IndexOutOfBoundsException(i);

        if (first.getItem() == null) {
            first.item = item;
            return;
        }

        Node<T> temp = first;
        for (int j = 0; temp != last; temp = temp.getNext(), j++)
            if (j == i) {
                temp.item = item;
                ++size;
                return;
            }

        throw new IllegalStateException("Cloud not add any item");
    }

    public T get(int i) {
        if (i < 0 || i > size)
            throw new IndexOutOfBoundsException(i);
        isFirstNull();

        Node<T> temp = first;
        for (int j = 0; temp != last; temp = temp.getNext(), j++)
            if (j == i)
                return temp.getItem();

        throw new IllegalStateException(" Could not get any item");
    }

    public T remove(int i) {
        if (i < 0 || i > size)
            throw new IndexOutOfBoundsException(i);
        isFirstNull();

        Node<T> temp = first;
        for (int j = 0; temp != last; temp = temp.getNext(), j++)
            if (j == i) {
                final T item = temp.item;
                final Node<T> next = temp.getNext();
                final Node<T> prev = temp.getPrev();
                temp = null;
                next.setPrev(prev);
                prev.setNext(next);
                --size;
                return item;
            }
        throw new IllegalStateException("Could not remove any item");
    }

    public void push(T item) {
        if (first.getItem() == null) {
            first.item = item;
            return;
        }
        Node<T> temp = first;

        while (true) {
            if (temp.getNext() == null) {
                temp.getNext().setItem(item);
                return;
            }
            temp = temp.getNext();
        }
    }

    public T find(T item) {
        isFirstNull();
        Node<T> temp = first;
        for (Node<T> x = first; x != last; x.getNext())
            if (temp.item == item)
                return temp.item;
        return null;
    }

    public T pop() {
        return removeFirst();
    }

    private void isFirstNull() {
        if (first.item == null)
            throw new java.util.NoSuchElementException();
    }
}
