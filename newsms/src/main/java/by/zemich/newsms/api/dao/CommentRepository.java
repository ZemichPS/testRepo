package by.zemich.newsms.api.dao;

import by.zemich.newsms.core.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID>, PagingAndSortingRepository<Comment, UUID> {

    @Override
    void deleteById(UUID id);

    Page<Comment> findAllByNewsId(UUID id, Pageable pageable);
    Optional<Comment> findByIdAndNewsId(UUID id, UUID newsId);
}
