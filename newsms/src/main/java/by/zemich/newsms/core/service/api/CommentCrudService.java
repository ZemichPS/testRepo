package by.zemich.newsms.core.service.api;

import by.zemich.newsms.core.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface CommentCrudService extends CrudService<Comment, UUID> {


    Page<Comment> findAllByNewsId(UUID id, Pageable pageable);

    Optional<Comment> findByIdAndNewsId(UUID commentId, UUID newsId);
}
