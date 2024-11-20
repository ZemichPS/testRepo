package by.zemich.newsms.api.controller.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;


@Data
public class CommentRequest {
    @NotNull(message = "newsId must not be null")
    private UUID newsId;

    @NotNull(message = "userId must not be null")
    private UUID userId;

    @NotNull(message = "username must not be null")
    @Size(min = 3, max = 50, message = "username must be between 3 and 50 characters")
    private String username;

    @NotNull(message = "text must not be null")
    @Size(min = 1, max = 500, message = "text must be between 1 and 500 characters")
    private String text;
}
