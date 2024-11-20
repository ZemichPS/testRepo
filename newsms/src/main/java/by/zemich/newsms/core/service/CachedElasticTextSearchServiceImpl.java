package by.zemich.newsms.core.service;

import by.zemich.newsms.api.dao.NewsElasticSearchRepository;
import by.zemich.newsms.core.domain.NewsDoc;
import by.zemich.newsms.core.service.api.ElasticNewsCrudService;
import by.zemich.newsms.core.service.api.FullTextSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@CacheConfig(
        cacheNames = "newsDocs"
)
public class CachedElasticTextSearchServiceImpl implements ElasticNewsCrudService, FullTextSearchService {

    private final NewsElasticSearchRepository newsElasticSearchRepository;

    @Override
    @Cacheable(key = "#id")
    public Optional<NewsDoc> findById(UUID id) {
        return newsElasticSearchRepository.findById(id);
    }

    @Override
    @CachePut(key = "#newsDoc.id")
    @CacheEvict(value = "newsDocPages", allEntries = true)
    public NewsDoc save(NewsDoc newsDoc) {
        return newsElasticSearchRepository.save(newsDoc);
    }

    @Override
    @Cacheable(
            value = "newsDocPages",
            key = "#pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort.toString()"

    )
    public Page<NewsDoc> findAll(String text, Pageable pageable) {
        return newsElasticSearchRepository.searchByQuery(text, pageable);
    }

    @Override
    @Cacheable(key = "#id")
    public boolean existsById(UUID id) {
        return newsElasticSearchRepository.existsById(id);
    }

    @Override
    @Caching( evict =  {
        @CacheEvict(key = "#id"),
        @CacheEvict(value = "newsDocPages", allEntries = true)
    })
    public void deleteById(UUID id) {
        newsElasticSearchRepository.deleteById(id);
    }

    @Override
    public Page<NewsDoc> findAll(Pageable pageable) {
        return newsElasticSearchRepository.findAll(pageable);
    }

}
