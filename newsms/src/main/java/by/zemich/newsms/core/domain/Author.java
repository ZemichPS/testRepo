package by.zemich.newsms.core.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.util.UUID;

@Embeddable
@Data
public class Author {
    @Column(name = "author_id")
    private UUID id;
    @Column(name = "author_username")
    private String username;
}
