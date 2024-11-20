package by.zemich.newsms.core.service.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import java.util.concurrent.Callable;

public class LFUCacheWrapper<K, V> implements Cache {

    private final LFUCache<K, V> cache;

    public LFUCacheWrapper(LFUCache<K, V> cache) {
        this.cache = cache;
    }

    @Override
    public String getName() {
        return "LFU cache";
    }

    @Override
    public Object getNativeCache() {
        return cache;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ValueWrapper get(Object key) {
        V value = cache.get((K)key);
        return (value != null) ? new SimpleValueWrapper(value) : null;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        V value = cache.get((K) key);
        return (value != null && type != null && type.isInstance(value)) ? (T) value : null;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        V value = cache.get((K) key);
        if (value != null) {
            return (T) value;
        }
        try {
            T loadedValue = valueLoader.call();
            put(key, loadedValue);
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
        cache.remove((K) key);
    }

    @Override
    public void clear() {
        cache.clear();
    }
}
