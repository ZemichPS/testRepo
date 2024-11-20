package by.zemich.newsms.core.service.cache;

import org.springframework.cache.Cache;

import java.util.concurrent.Callable;

public class LruCacheWrapper<K, V> implements Cache {

    private final LRUCache<K, V> cache;

    public LruCacheWrapper(LRUCache<K, V> cache) {
        this.cache = cache;
    }

    @Override
    public String getName() {
        return "LRU Cache";
    }

    @Override
    public Object getNativeCache() {
        return cache;
    }

    @Override
    public ValueWrapper get(Object key) {
        V value = cache.get(key);
        return (value != null ? () -> value : null);
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        V value = cache.get(key);
        if (value != null && type.isInstance(value)) {
            return (T) value;
        }
        return null;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        V value = cache.get(key);
        if (value != null) {
            return (T) value;
        }
        try {
            T loadedValue = valueLoader.call();
            cache.put((K) key, (V) loadedValue);
            return loadedValue;
        } catch (Exception e) {
            throw new RuntimeException("Error loading value for key: " + key, e);
        }
    }

    @Override
    public void put(Object key, Object value) {
        cache.put((K) key, (V) value);
    }

    @Override
    public void evict(Object key) {
        cache.remove(key);
    }

    @Override
    public void clear() {
        cache.clear();
    }
}
