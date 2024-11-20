package by.zemich.newsms.core.service.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LRUCacheManager implements CacheManager {

    private final Map<String, Cache> caches = new ConcurrentHashMap<>();
    private final int maxSize;


    public LRUCacheManager(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public Cache getCache(String name) {
        return caches.computeIfAbsent(name, k -> new LruCacheWrapper<>(new LRUCache(maxSize)));
    }

    @Override
    public Collection<String> getCacheNames() {
        return Collections.unmodifiableSet(caches.keySet());
    }
}
