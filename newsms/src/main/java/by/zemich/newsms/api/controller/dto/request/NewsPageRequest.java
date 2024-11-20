package by.zemich.newsms.api.controller.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class NewsPageRequest {
    @NotNull(message = "id must not be null")
    private UUID id;
    @Min(value = 0, message = "pageNumber must be 0 or greater")
    private int pageNumber;
    @Min(value = 1, message = "pageSize must be 1 or greater")
    private int pageSize;
    private String sortBy;
}
