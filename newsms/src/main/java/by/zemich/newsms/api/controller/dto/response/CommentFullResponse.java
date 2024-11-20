package by.zemich.newsms.api.controller.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CommentFullResponse {
    private UUID id;
    private UUID newsId;
    private String authorId;
    private String authorUsername;
    private LocalDateTime createdAt;
    private String text;
}
