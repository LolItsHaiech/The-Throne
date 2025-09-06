package util.map;

import util.LinkedList;

import java.io.Serializable;
import java.util.Objects;

public class Map<K, V> extends LinkedList<MapEntry<K, V>> implements Serializable {
    public V get(K key) {
        for (MapEntry<K, V> entry : this) {
            if (Objects.equals(entry.getKey(), key)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public void set(K key, V value) {
        boolean changed = false;
        for (MapEntry<K, V> entry : this) {
            if (Objects.equals(entry.getKey(), key)) {
                entry.setValue(value);
                changed = true;
            }
        }
        if (!changed) {
            this.addFirst(new MapEntry<>(key, value));
        }
    }

    public void removeFromKey(K key) {
        for (MapEntry<K, V> kvMapEntry : this) {
            if (kvMapEntry.getKey() == key) {
                this.remove(kvMapEntry);
                return;
            }
        }
    }

    public void addFirst(K key, V value) {
        super.addFirst(new MapEntry<>(key, value));
    }

    public void addLast(K key, V value) {
        super.addLast(new MapEntry<>(key, value));
    }

    public MapEntry<K, V> get(int i) {
        return super.get(i);
    }

    public boolean containsKey(K key) {
        for (MapEntry<K, V> entry : this) {
            if (Objects.equals(entry.getKey(), key)) {
                return true;
            }
        }
        return false;
    }
}
