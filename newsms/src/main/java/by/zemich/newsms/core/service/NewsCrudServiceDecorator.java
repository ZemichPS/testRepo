package by.zemich.newsms.core.service;

import by.zemich.newsms.core.domain.News;
import by.zemich.newsms.core.mapper.NewsDocMapper;
import by.zemich.newsms.core.service.api.ElasticNewsCrudService;
import by.zemich.newsms.core.service.api.NewsCrudService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class NewsCrudServiceDecorator implements NewsCrudService {

    private final NewsCrudService newsCrudService;
    private final NewsDocMapper newsDocMapper;
    private final ElasticNewsCrudService elasticNewsCrudService;

    public NewsCrudServiceDecorator(
            @Qualifier("cachedNewsCrudServiceImpl") NewsCrudService newsCrudService,
            NewsDocMapper newsDocMapper,
            ElasticNewsCrudService elasticNewsCrudService
    ) {
        this.newsCrudService = newsCrudService;
        this.newsDocMapper = newsDocMapper;
        this.elasticNewsCrudService = elasticNewsCrudService;
    }

    @Override
    public News save(News news) {
        News saved = newsCrudService.save(news);
        elasticNewsCrudService.findById(saved.getId())
                .map(newsDoc -> {
                    newsDocMapper.mapEntityToExistingDoc(news, newsDoc);
                    return newsDoc;
                })
                .map(elasticNewsCrudService::save)
                .or(() -> Optional.ofNullable(newsDocMapper.mapEntityToDoc(news)))
                .map(elasticNewsCrudService::save);
        return saved;
    }

    @Override
    public Optional<News> findById(UUID id) {
        return newsCrudService.findById(id);
    }

    @Override
    public void deleteById(UUID id) {
        newsCrudService.deleteById(id);
        elasticNewsCrudService.deleteById(id);
    }

    @Override
    public Page<News> findAll(Pageable pageable) {
        return newsCrudService.findAll(pageable);
    }

    @Override
    public boolean existsById(UUID id) {
        return newsCrudService.existsById(id);
    }
}
