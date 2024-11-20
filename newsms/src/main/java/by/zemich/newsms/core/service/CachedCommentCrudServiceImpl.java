package by.zemich.newsms.core.service;

import by.zemich.newsms.api.dao.CommentRepository;
import by.zemich.newsms.core.domain.Comment;
import by.zemich.newsms.core.service.api.CommentCrudService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@CacheConfig(cacheNames = "comments")
@AllArgsConstructor
public class CachedCommentCrudServiceImpl implements CommentCrudService {

    private final CommentRepository commentRepository;

    @Override
    @CachePut(value = "comments", key = "#comment.id")
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    @Cacheable(key = "#id")
    @Transactional(readOnly = true)
    public Optional<Comment> findById(UUID id) {
        return commentRepository.findById(id);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "comments", key = "#id"),
            @CacheEvict(value = "commentPages", allEntries = true)
    })
    public void deleteById(UUID id) {
        commentRepository.deleteById(id);
    }

    @Override
    @Cacheable(
            value = "commentPages",
            key = "#pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort.toString()"
    )
    @Transactional(readOnly = true)
    public Page<Comment> findAll(Pageable pageable) {
        return commentRepository.findAll(pageable);
    }

    @Override
    @Cacheable(key = "id")
    public boolean existsById(UUID id) {
        return commentRepository.existsById(id);
    }

    @Override
    @Cacheable(
            value = "commentPages",
            key = "#id + '_' + #pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort.toString()"
    )
    @Transactional(readOnly = true)
    public Page<Comment> findAllByNewsId(UUID id, Pageable pageable) {
        return commentRepository.findAllByNewsId(id, pageable);
    }

    // TODO проверить на корректность работы

    @Override
    @Cacheable(key = "#commentId + '_' + #newsId")
    public Optional<Comment> findByIdAndNewsId(UUID commentId, UUID newsId) {
        return commentRepository.findByIdAndNewsId(commentId, newsId);
    }
}
