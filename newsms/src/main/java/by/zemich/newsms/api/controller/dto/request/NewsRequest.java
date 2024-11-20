package by.zemich.newsms.api.controller.dto.request;

import by.zemich.newsms.core.domain.Comment;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsRequest {
    @NotNull(message = "authorId must not be null")
    private UUID authorId;

    @NotNull(message = "authorUsername must not be null")
    @Size(min = 3, max = 50, message = "author username must be between 3 and 50 characters")
    private String authorUsername;

    @NotNull(message = "title must not be null")
    @Size(min = 5, max = 100, message = "title must be between 5 and 100 characters")
    private String title;

    @NotNull(message = "text must not be null")
    @Size(min = 1, max = 2000, message = "text must be between 1 and 2000 characters")
    private String text;
}
