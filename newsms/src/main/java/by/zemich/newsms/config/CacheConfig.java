package by.zemich.newsms.config;

import by.zemich.newsms.core.service.cache.LFUCacheManager;
import by.zemich.newsms.core.service.cache.LRUCacheManager;
import by.zemich.newsms.properties.CacheProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@EnableCaching
@Configuration
@RequiredArgsConstructor
@Profile(value = "test")
@ConditionalOnProperty(name = "cache.enabled", havingValue = "true")
public class CacheConfig {

    @Bean
    @ConditionalOnProperty(
            name = "cache.algorithm",
            havingValue = "LRU"
    )
    public CacheManager lruCacheManager(CacheProperties properties) {
        return new LRUCacheManager(properties.getSize());
    }


    @Bean
    @ConditionalOnProperty(
            name = "cache.algorithm",
            havingValue = "LFU"
    )
    public CacheManager lfuCacheManager(CacheProperties cacheProperties) {
        return new LFUCacheManager(cacheProperties.getSize());
    }

}
