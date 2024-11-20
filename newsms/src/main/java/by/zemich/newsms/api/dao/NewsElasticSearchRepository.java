package by.zemich.newsms.api.dao;

import by.zemich.newsms.core.domain.NewsDoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface NewsElasticSearchRepository extends ElasticsearchRepository<NewsDoc, UUID> {

    @Query("""
            {
              "multi_match": {
                "query": "?0",
                "fields": ["text^2", "title"],
                "type": "most_fields",
                "analyzer": "russian"
              }
            }
            """)
    Page<NewsDoc> searchByQuery(@Param("text") String query, Pageable pageable);
}
