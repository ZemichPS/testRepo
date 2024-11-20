package by.zemich.newsms.core.service.api;

import by.zemich.newsms.core.domain.NewsDoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FullTextSearchService {
    Page<NewsDoc> findAll(String text, Pageable pageable);
}
