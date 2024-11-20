package by.zemich.newsms.api.controller;

import by.zemich.newsms.api.controller.dto.request.NewsRequest;
import by.zemich.newsms.api.controller.dto.response.CommentFullResponse;
import by.zemich.newsms.api.controller.dto.response.NewsFullResponse;
import by.zemich.newsms.api.controller.dto.response.ShortCommentResponse;
import by.zemich.newsms.core.domain.News;
import by.zemich.newsms.core.service.NewsRestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
@Tag(
        name = "News management endpoints"
)
public class NewsController extends BaseController{

    private final NewsRestService newsRestService;

    @Operation(
            description = "add new news"
    )
    @PostMapping
    public ResponseEntity<URI> create(@RequestBody @Valid NewsRequest newsRequest) {
        News savedNews = newsRestService.save(newsRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedNews.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @Operation(
            description = "get page news"
    )
    @GetMapping
    public ResponseEntity<PageImpl<NewsFullResponse>> getNews(
            @RequestParam(name = "page_number", defaultValue = "0") int pageNumber,
            @RequestParam(name = "page_size", defaultValue = "10") int pageSize,
            @RequestParam(name = "sort_by", defaultValue = "createdAt") String sortBy
    ) {

        PageRequest pageRequest = PageRequest.of(
                pageNumber,
                pageSize,
                Sort.by(Sort.Direction.DESC, sortBy)
        );

        PageImpl<NewsFullResponse> page = newsRestService.getNews(pageRequest);
        return ResponseEntity.ok(page);
    }

    @Operation(
            description = "full text search"
    )
    @GetMapping("/full")
    public ResponseEntity<Page<NewsFullResponse>> search(
            @RequestParam(name = "page_number", defaultValue = "0") int pageNumber,
            @RequestParam(name = "page_size", defaultValue = "10") int pageSize,
            @RequestParam String text
    ) {
        PageRequest pageRequest = PageRequest.of(
                pageNumber,
                pageSize
        );
        Page<NewsFullResponse> response = newsRestService.fullTextSearch(text, pageRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(
            description = "get news by id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<NewsFullResponse> getById(@PathVariable UUID id) {
        NewsFullResponse newsFullResponse = newsRestService.findById(id);
        return ResponseEntity.ok(newsFullResponse);
    }

    @Operation(
            description = "get comments page by news id"
    )
    @GetMapping("/{id}/comments")
    public ResponseEntity<PageImpl<ShortCommentResponse>> getNews(
            @PathVariable UUID id,
            @RequestParam(name = "page_number", defaultValue = "0") int pageNumber,
            @RequestParam(name = "page_size", defaultValue = "10") int pageSize,
            @RequestParam(name = "sort_by", defaultValue = "createdAt") String sortBy
    ) {

        PageRequest pageRequest = PageRequest.of(
                pageNumber,
                pageSize,
                Sort.by(Sort.Direction.DESC, sortBy)
        );

        PageImpl<ShortCommentResponse> page = newsRestService.getCommentsPage(id, pageRequest);
        return ResponseEntity.ok(page);
    }

    @Operation(
            description = "get comment by id and news id"
    )
    @GetMapping("/{news_id}/comments/{comment_id}")
    public ResponseEntity<CommentFullResponse> getCommentByNewsId(
            @PathVariable(name = "news_id") UUID newsId,
            @PathVariable(name = "comment_id") UUID commentId
    ) {
        CommentFullResponse response = newsRestService.getCommentByNewsIdAndCommentId(newsId, commentId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            description = "update news by id"
    )
    @PutMapping("/{id}")
    public ResponseEntity<NewsFullResponse> updateById(
            @PathVariable UUID id,
            @RequestBody @Valid NewsRequest newsRequest
    ) {
        NewsFullResponse newsFullResponse = newsRestService.update(id, newsRequest);
        return ResponseEntity.ok(newsFullResponse);
    }

    @Operation(
            description = "partial update news by id"
    )
    @PatchMapping("/{id}")
    public ResponseEntity<NewsFullResponse> patchById(
            @PathVariable UUID id,
            @RequestBody @Valid NewsRequest newsRequest
    ) {
        NewsFullResponse newsFullResponse = newsRestService.patchById(id, newsRequest);
        return ResponseEntity.ok(newsFullResponse);
    }

    @Operation(
            description = "delete news by id"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        newsRestService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
