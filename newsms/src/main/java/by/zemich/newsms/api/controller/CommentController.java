package by.zemich.newsms.api.controller;

import by.zemich.newsms.api.controller.dto.request.CommentRequest;
import by.zemich.newsms.api.controller.dto.response.CommentFullResponse;
import by.zemich.newsms.core.domain.Comment;
import by.zemich.newsms.core.service.CommentRestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Tag(
        name = "Comment management endpoints"
)
public class CommentController extends BaseController {

    private final CommentRestService commentRestService;

    @Operation(
            description = "get comment by comment id"
    )
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentFullResponse> getById(@PathVariable UUID commentId) {
        CommentFullResponse comment = commentRestService.findById(commentId);
        return ResponseEntity.ok(comment);
    }

    @Operation(
            description = "add new comment"
    )
    @PostMapping
    public ResponseEntity<URI> create(@RequestBody @Valid CommentRequest newCommentRequest) {
        Comment savedComment = commentRestService.save(newCommentRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{commentId}")
                .buildAndExpand(savedComment.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @Operation(
            description = "update comment by comment id"
    )
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentFullResponse> updateById(@PathVariable UUID commentId, @RequestBody CommentRequest newCommentRequest) {
        CommentFullResponse updatedComment = commentRestService.update(commentId, newCommentRequest);
        return ResponseEntity.ok(updatedComment);
    }

    @Operation(
            description = "partial update comment by comment id"
    )
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentFullResponse> patchById(
            @PathVariable UUID commentId,
            @RequestBody @Valid CommentRequest commentRequest
    ) {
        CommentFullResponse updatedComment = commentRestService.partialUpdateUpdate(commentId, commentRequest);
        return ResponseEntity.ok(updatedComment);
    }

    @Operation(
            description = "delete comment by comment id"
    )
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID commentId) {
        commentRestService.delete(commentId);
        return ResponseEntity.noContent().build();
    }


}
