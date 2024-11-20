package by.zemich.newsms.core.service;

import by.zemich.newsms.api.controller.dto.request.NewsRequest;
import by.zemich.newsms.api.controller.dto.response.CommentFullResponse;
import by.zemich.newsms.api.controller.dto.response.NewsFullResponse;
import by.zemich.newsms.api.controller.dto.response.ShortCommentResponse;
import by.zemich.newsms.core.domain.Comment;
import by.zemich.newsms.core.domain.News;
import by.zemich.newsms.core.domain.NewsDoc;
import by.zemich.newsms.core.mapper.CommentMapper;
import by.zemich.newsms.core.mapper.NewsMapper;
import by.zemich.newsms.core.service.api.CommentCrudService;
import by.zemich.newsms.core.service.api.FullTextSearchService;
import by.zemich.newsms.core.service.api.NewsCrudService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class NewsRestService {
    private final NewsCrudService newsCrudService;
    private final CommentCrudService commentCrudService;
    private final FullTextSearchService fullTextSearchService;
    private final CommentMapper commentMapper;
    private final NewsMapper newsMapper;

    public NewsRestService(
            @Qualifier("newsCrudServiceDecorator") NewsCrudService newsCrudService,
            CommentCrudService commentCrudService,
            CommentMapper commentMapper,
            NewsMapper newsMapper,
            FullTextSearchService fullTextSearchService
    ) {
        this.newsCrudService = newsCrudService;
        this.commentCrudService = commentCrudService;
        this.commentMapper = commentMapper;
        this.newsMapper = newsMapper;
        this.fullTextSearchService = fullTextSearchService;
    }

    public News save(NewsRequest newsRequest) {
        News news = newsMapper.mapToEntity(newsRequest);
        return newsCrudService.save(news);
    }

    public NewsFullResponse findById(UUID id) {
        return newsCrudService.findById(id)
                .map(newsMapper::mapToFullResponse)
                .orElseThrow(
                        () -> new EntityNotFoundException("News with id %s is nowhere to be found".formatted(id.toString()))
                );
    }

    public void deleteById(UUID id) {
        newsCrudService.deleteById(id);
    }

    public NewsFullResponse patchById(UUID id, NewsRequest newsRequest) {
        return newsCrudService.findById(id)
                .map(news -> {
                    newsMapper.partialMapToExistingEntity(newsRequest, news);
                    return news;
                })
                .map(newsCrudService::save)
                .map(newsMapper::mapToFullResponse)
                .orElseThrow(
                        () -> new EntityNotFoundException("News with id %s is nowhere to be found".formatted(id.toString()))
                );
    }

    public NewsFullResponse update(UUID id, NewsRequest newsRequest) {
        return newsCrudService.findById(id)
                .map(news -> {
                    newsMapper.mapToExistingEntity(newsRequest, news);
                    return news;
                })
                .map(newsCrudService::save)
                .map(newsMapper::mapToFullResponse)
                .orElseThrow(
                        () -> new EntityNotFoundException("News with id %s is nowhere to be found".formatted(id.toString()))
                );
    }


    public PageImpl<ShortCommentResponse> getCommentsPage(UUID newsId, PageRequest pageRequest) {
        Page<Comment> allCommentsPage = commentCrudService.findAllByNewsId(newsId, pageRequest);
        List<ShortCommentResponse> responses = allCommentsPage.get()
                .map(commentMapper::mapToDTO)
                .toList();
        return new PageImpl<>(responses, pageRequest, allCommentsPage.getTotalElements());
    }

    public CommentFullResponse getCommentByNewsIdAndCommentId(UUID newsId, UUID commentId) {
        if (!newsCrudService.existsById(newsId))
            throw new EntityNotFoundException("News with id %s is nowhere to be found".formatted(newsId.toString()));
        return commentCrudService.findByIdAndNewsId(commentId, newsId)
                .map(commentMapper::mapToFullResponse)
                .orElseThrow(
                        () -> new EntityNotFoundException("Comment with id %s is nowhere to be found".formatted(commentId.toString()))
                );
    }

    public PageImpl<NewsFullResponse> getNews(PageRequest pageRequest) {
        Page<News> newsPage = newsCrudService.findAll(pageRequest);
        final List<NewsFullResponse> fullResponses = newsPage.getContent().stream()
                .map(newsMapper::mapToFullResponse)
                .toList();
        return new PageImpl<>(fullResponses, pageRequest, newsPage.getTotalElements());
    }

    public Page<NewsFullResponse> fullTextSearch(String text, PageRequest pageRequest) {
        List<NewsFullResponse> news = fullTextSearchService.findAll(text, pageRequest)
                .getContent().stream()
                .map(NewsDoc::getId)
                .map(newsCrudService::findById)
                .flatMap(Optional::stream)
                .map(newsMapper::mapToFullResponse)
                .toList();

        return new PageImpl<>(news, pageRequest, news.size());
    }
}
