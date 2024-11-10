package tech.infinitymz.lib.collections;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import lombok.Getter;

/**
 * LinkedList
 */
@SuppressWarnings({ "unused", "unchecked" })
public class HashTable<K, E> {
    // @AllArgsConstructor
    @Getter
    public class Entry<T, V> {
        final private T key;
        private V value;

        public <J, M> Entry(J key, M value) {
            this.key = (T) key;
            this.value = (V) value;
        }

        public static <K> int hashCode(K key, int size) {
            int hash = generateHash(key, size);
            double logic = (Math.sqrt(5) - 1) / 2;
            return (int) Math.floor(size * ((logic * hash) % 1));
        }

        private static <K> int generateHash(K key, int size) {
            int i = 0;
            if (key instanceof Integer)
                return (int) key;
            if (key instanceof Double) {
                i = (int) key / 1;
                i += (int) key % 1;
                return Math.abs(i);
            }
            // If the key is null, it will transform into string, say "NO" to exception!!!
            String instance = (String) key;
            if (key instanceof String) {
                for (int j = 0; j < instance.length(); i++) {
                    j += instance.charAt(i) * Math.pow(size, j);
                    return j;
                }
            }
            return -1;
        }

    }

    private final double LOAD_FACTOR = 0.75;
    private LinkedList<Entry<K, E>>[] buckets;
    private int capacity, occupiedCapacity;

    public HashTable() {
        initMap(0);
    }

    private void initMap(int capacity) {
        this.capacity = capacity > 0 ? capacity : 11;
        occupiedCapacity = 0;
        buckets = new LinkedList[this.capacity];
    }

    private void rehash() {
        if (occupiedCapacity < capacity * LOAD_FACTOR)
            return;
        this.capacity = (capacity << 1) + 1;
        LinkedList<Entry<K, E>>[] oldBucket = buckets;
        this.buckets = new LinkedList[capacity];
        this.occupiedCapacity = 0;
        Entry<K, E> entry = null;
        for (LinkedList<Entry<K, E>> old : oldBucket)
            if (old != null)
                for (int j = 0, hashIndex = 0; j < old.size(); j++) {
                    entry = old.pop();
                    hashIndex = Entry.hashCode(entry.getKey(), this.capacity);
                    if (buckets[hashIndex] == null) {
                        ++occupiedCapacity;
                        buckets[hashIndex] = new LinkedList<>();
                    }
                    buckets[hashIndex].push(entry);
                }

    }

    public <T, V> void add(T key, V value) {

        rehash();
        Entry<K, E> item = new Entry<K, E>(key, value);
        int hashIndex = Entry.hashCode(key, capacity);
        if (buckets[hashIndex] == null) {
            ++occupiedCapacity;
            buckets[hashIndex] = new LinkedList<>();
        }
        buckets[hashIndex].push(item);
    }

    @SuppressWarnings("unlikely-arg-type")
    public <T, G> Entry<K, E> remove(T key) {
        if (find(key) == null)
            throw new NoSuchElementException();

        int hashIndex = Entry.hashCode(key, capacity);
        Entry<K, E> temp = null;

        for (int i = 0; i < buckets[hashIndex].size(); i++)
            if (buckets[hashIndex].get(i).getKey().equals(key)) {
                temp = buckets[hashIndex].remove(i);
                return temp;
            }
        throw new IllegalStateException("Could not remove any item");
    }

    @SuppressWarnings("unlikely-arg-type")
    public <T> Entry<K, E> find(T key) {
        int hashIndex = Entry.hashCode(key, capacity);
        if (buckets[hashIndex] == null)
            return null;
        Entry<K, E> temp = null;
        for (int i = 0; i < buckets[hashIndex].size(); i++) {
            temp = buckets[hashIndex].get(i);
            if (temp != null)
                if (temp.getKey().equals(key))
                    return temp;
        }

        return null;
    }

    public List<E> getValues() {
        List<E> values = new ArrayList<>();
        for (LinkedList<Entry<K, E>> bucket : buckets)
            if (bucket != null)
                for (int i = 0; i < bucket.size(); i++)
                    values.add(bucket.get(i).getValue());
        return values;
    }

    public List<K> getKeys() {
        List<K> keys = new ArrayList<>();
        for (LinkedList<Entry<K, E>> bucket : buckets)
            if (bucket != null)
                for (int i = 0; i < bucket.size(); i++)
                    keys.add(bucket.get(i).getKey());

        return keys;
    }
}
