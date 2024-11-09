package tech.infinitymz.lib.collections;

import java.util.NoSuchElementException;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * LinkedList
 */
@SuppressWarnings({ "unused", "unchecked" })
public class HashTable {
    @AllArgsConstructor
    @Getter
    private class Entry<T, V> {
        final private T key;
        private V value;

        public static <K> int hashCode(K key, int size) {
            int hash = Math.abs(genHash(key, size));
            double logic = (Math.sqrt(5) - 1) / 2;
            return (int) Math.floor(size * ((logic * hash) % 1));
        }

        private static <K> int genHash(K key, int size) {
            int i = 0;
            if (key instanceof Integer)
                return (int) key;
            if (key instanceof Double) {
                i = (int) key / 1;
                i += (int) key % 1;
                return i;
            }
            if (key instanceof String) {
                for (int j = 0; j < ((String) key).length(); i++) {
                    j += ((String) key).charAt(i) * Math.pow(size, j);
                    return j;
                }
            }
            return -1;
        }
    }

    private final double LOAD_FACTOR = 0.75;
    private LinkedList<Entry<?, ?>>[] buckets;
    private int capacity, occupiedCapacity;

    public HashTable() {
        initMap(0);
    }

    private void initMap(int capacity) {
        capacity = capacity > 0 ? capacity : 11;
        occupiedCapacity = 0;
        buckets = new LinkedList[capacity];
    }

    private void rehash() {
        if (occupiedCapacity < capacity * LOAD_FACTOR)
            return;
        capacity = (capacity << 1) + 1;
        LinkedList<Entry<?, ?>>[] oldBucket = buckets;
        buckets = new LinkedList[capacity];
        for (int i = occupiedCapacity; i < occupiedCapacity; i++)
            for (int j = 0; j < oldBucket[i].size(); j++)
                buckets[i].push(oldBucket[i].pop());

    }

    public <K, V> void add(K key, V value) {

        rehash();
        var item = new Entry<>(key, value);
        int hashIndex = Entry.hashCode(key, capacity);
        if (buckets[hashIndex] == null)
            buckets[hashIndex] = new LinkedList<>();
        buckets[hashIndex].push(item);
    }

    public <K> Entry<?, ?> remove(K key) {
        if (find(key) == null)
            throw new NoSuchElementException();

        int hashIndex = Entry.hashCode(key, capacity);

        Entry<?, ?> temp = null;
        for (int i = 0; i < buckets[hashIndex].size(); i++)
            if (buckets[hashIndex].get(i).getKey().equals(key)) {
                temp = buckets[hashIndex].get(i);
                // buckets[hashIndex].
                return temp;
            }
        throw new IllegalStateException("Could not remove any item");
    }

    public <K> Entry<?, ?> find(K key) {
        int hashIndex = Entry.hashCode(key, capacity);
        if (buckets[hashIndex] == null)
            return null;
        Entry<?, ?> temp = null;
        for (int i = 0; i < buckets[hashIndex].size(); i++, temp = buckets[hashIndex].get(i))
            if (temp != null)
                if (temp.getKey().equals(key))
                    return temp;

        return null;
    }
}