package by.zemich.newsms.api.controller.dto.response;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ShortCommentResponse {
    private UUID id;
    private UUID authorId;
    private String authorUsername;
    private LocalDateTime createdAt;
    private String text;
    private String username;
}
