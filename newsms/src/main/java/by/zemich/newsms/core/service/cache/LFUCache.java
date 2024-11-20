package by.zemich.newsms.core.service.cache;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class LFUCache<K, V> {
    private final int capacity;
    private int minFrequency;
    private final Map<K, V> cache;
    private final Map<K, Integer> frequencyMap;
    private final Map<Integer, LinkedHashSet<K>> frequencyListMap;

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.minFrequency = 0;
        this.cache = new HashMap<>();
        this.frequencyMap = new HashMap<>();
        this.frequencyListMap = new HashMap<>();
    }

    public V get(K key) {
        if (!cache.containsKey(key)) {
            return null;
        }
        int frequency = frequencyMap.get(key);
        frequencyMap.put(key, frequency + 1);

        frequencyListMap.get(frequency).remove(key);
        if (frequencyListMap.get(frequency).isEmpty()) {
            frequencyListMap.remove(frequency);
            if (minFrequency == frequency) {
                minFrequency++;
            }
        }
        frequencyListMap.computeIfAbsent(frequency + 1, k -> new LinkedHashSet<>()).add(key);
        return cache.get(key);
    }

    public void put(K key, V value) {
        if (capacity == 0) return;

        if (cache.containsKey(key)) {
            cache.put(key, value);
            get(key);
            return;
        }

        if (cache.size() >= capacity) {
            K evictKey = frequencyListMap.get(minFrequency).iterator().next();
            frequencyListMap.get(minFrequency).remove(evictKey);
            if (frequencyListMap.get(minFrequency).isEmpty()) {
                frequencyListMap.remove(minFrequency);
            }
            cache.remove(evictKey);
            frequencyMap.remove(evictKey);
        }

        cache.put(key, value);
        frequencyMap.put(key, 1);
        minFrequency = 1;
        frequencyListMap.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(key);
    }

    public void remove(K key) {
        if (!cache.containsKey(key)) return;

        int frequency = frequencyMap.get(key);
        frequencyListMap.get(frequency).remove(key);
        if (frequencyListMap.get(frequency).isEmpty()) {
            frequencyListMap.remove(frequency);
            if (minFrequency == frequency) {
                minFrequency++;
            }
        }
        cache.remove(key);
        frequencyMap.remove(key);
    }

    public void clear() {
        cache.clear();
        frequencyMap.clear();
        frequencyListMap.clear();
    }
}
