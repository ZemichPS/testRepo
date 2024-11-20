package by.zemich.newsms.core.service;

import by.zemich.newsms.api.controller.dto.request.CommentRequest;
import by.zemich.newsms.api.controller.dto.response.CommentFullResponse;
import by.zemich.newsms.core.domain.Comment;
import by.zemich.newsms.core.domain.News;
import by.zemich.newsms.core.mapper.CommentMapper;
import by.zemich.newsms.core.service.api.CommentCrudService;
import by.zemich.newsms.core.service.api.NewsCrudService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class CommentRestService {

    private final CommentCrudService commentCrudService;
    private final NewsCrudService newsCrudService;
    private final CommentMapper commentMapper;

    public CommentRestService(
            CommentCrudService commentCrudService,
            @Qualifier("cachedNewsCrudServiceImpl") NewsCrudService newsCrudService,
            CommentMapper commentMapper
    ) {
        this.commentCrudService = commentCrudService;
        this.newsCrudService = newsCrudService;
        this.commentMapper = commentMapper;
    }

    public Comment save(CommentRequest commentRequest) {
        UUID newsId = commentRequest.getNewsId();
        News news = newsCrudService.findById(commentRequest.getNewsId())
                .orElseThrow(() -> new EntityNotFoundException("News with id %s is nowhere to be found".formatted(newsId)));
        Comment newComment = commentMapper.mapToEntity(commentRequest);
        newComment.setNews(news);
        commentCrudService.save(newComment);
        return newComment;
    }

    public CommentFullResponse update(UUID id, CommentRequest commentRequest) {
        return commentCrudService.findById(id)
                .map(comment -> {
                    commentMapper.mapToExistingEntity(commentRequest, comment);
                    return comment;
                })
                .map(commentCrudService::save)
                .map(commentMapper::mapToFullResponse)
                .orElseThrow(() -> new EntityNotFoundException(id.toString()));
    }

    public CommentFullResponse findById(UUID id) {
        return commentCrudService.findById(id)
                .map(commentMapper::mapToFullResponse)
                .orElseThrow(() -> new EntityNotFoundException(id.toString()));
    }

    public void delete(UUID id) {
        commentCrudService.deleteById(id);
    }

    public CommentFullResponse partialUpdateUpdate(UUID id, CommentRequest commentRequest) {
        return commentCrudService.findById(id)
                .map(comment -> {
                    commentMapper.partialMapToExistingEntity(commentRequest, comment);
                    return comment;
                })
                .map(commentCrudService::save)
                .map(commentMapper::mapToFullResponse)
                .orElseThrow(
                        () -> new EntityNotFoundException("Comment with id: %s is nowhere to be found".formatted(id.toString()))
                );
    }
}
