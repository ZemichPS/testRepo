package by.zemich.newsms.api.controller.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class NewsFullResponse {
    private UUID id;
    private UUID authorId;
    private String authorUsername;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String title;
    private String text;
}
