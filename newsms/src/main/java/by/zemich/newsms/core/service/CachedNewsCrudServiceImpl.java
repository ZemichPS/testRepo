package by.zemich.newsms.core.service;

import by.zemich.newsms.api.dao.NewsRepository;
import by.zemich.newsms.core.domain.News;
import by.zemich.newsms.core.service.api.NewsCrudService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@CacheConfig(cacheNames = "news")
@AllArgsConstructor
public class CachedNewsCrudServiceImpl implements NewsCrudService {

    private final NewsRepository newsRepository;

    @Override
    @CachePut(key = "#news.id")
    @CacheEvict(
            value = "newsPages",
            allEntries = true
    )
    public News save(News news) {
        return newsRepository.save(news);
    }

    @Override
    @Cacheable(key = "#id")
    @Transactional(readOnly = true)
    public Optional<News> findById(UUID id) {
        return newsRepository.findById(id);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(key = "#id"),
            @CacheEvict(value = "newsPages", allEntries = true)
    })
    public void deleteById(UUID id) {
        newsRepository.deleteById(id);
    }

    @Override
    @Cacheable(
            value = "newsPages",
            key = "#pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort.toString()"
    )
    @Transactional(readOnly = true)
    public Page<News> findAll(Pageable pageable) {
        return newsRepository.findAll(pageable);
    }

    @Override
    @Cacheable(key = "#id")
    public boolean existsById(UUID id) {
        return newsRepository.existsById(id);
    }
}
