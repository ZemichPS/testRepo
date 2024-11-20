package by.zemich.newsms.core.mapper;

import by.zemich.newsms.core.domain.News;
import by.zemich.newsms.core.domain.NewsDoc;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface NewsDocMapper {

    @Mapping(source = "author.id", target = "authorId")
    NewsDoc mapEntityToDoc(News news);

    @Mapping(source = "author.id", target = "authorId")
    void mapEntityToExistingDoc(News news, @MappingTarget NewsDoc newsDoc);

}
