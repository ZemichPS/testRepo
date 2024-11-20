package by.zemich.newsms.core.domain;

import lombok.Data;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Document(indexName = "news")
public class NewsDoc {
    @Id
    private UUID id;
    @Field(name = "author_id", type = FieldType.Keyword)
    private UUID authorId;
    @Field(type = FieldType.Text)
    private String title;
    @Field(type = FieldType.Text)
    private String text;
}
