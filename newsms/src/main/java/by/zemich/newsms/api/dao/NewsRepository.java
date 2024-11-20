package by.zemich.newsms.api.dao;

import by.zemich.newsms.core.domain.News;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NewsRepository extends JpaRepository<News, UUID> {
    @Override
    void deleteById(UUID id);
}
