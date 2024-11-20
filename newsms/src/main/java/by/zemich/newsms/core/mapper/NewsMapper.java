package by.zemich.newsms.core.mapper;

import by.zemich.newsms.api.controller.dto.request.NewsRequest;
import by.zemich.newsms.api.controller.dto.response.NewsFullResponse;
import by.zemich.newsms.core.domain.Author;
import by.zemich.newsms.core.domain.News;
import org.mapstruct.*;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface NewsMapper {

    @Mapping(target = "author", source = ".", qualifiedByName = "mapAuthor")
    News mapToEntity(NewsRequest newsRequest);

    @Mapping(target = "author", source = ".", qualifiedByName = "mapAuthor")
    void mapToExistingEntity(NewsRequest newsRequest, @MappingTarget News news);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "author", source = ".", qualifiedByName = "mapAuthor")
    void partialMapToExistingEntity(NewsRequest newsRequest, @MappingTarget News news);

    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "author.username", target = "authorUsername")
    NewsFullResponse mapToFullResponse(News news);

    @Named("mapAuthor")
    default Author mapAuthor(NewsRequest newsRequest) {
        Author author = new Author();
        author.setId(newsRequest.getAuthorId());
        author.setUsername(newsRequest.getAuthorUsername());
        return author;
    }



}
