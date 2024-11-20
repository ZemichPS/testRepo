package by.zemich.newsms.core.mapper;

import by.zemich.newsms.api.controller.dto.request.CommentRequest;
import by.zemich.newsms.api.controller.dto.response.CommentFullResponse;
import by.zemich.newsms.api.controller.dto.response.ShortCommentResponse;
import by.zemich.newsms.core.domain.Author;
import by.zemich.newsms.core.domain.Comment;
import org.mapstruct.*;

import java.util.Map;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "author", source = ".", qualifiedByName = "toAuthor")
    Comment mapToEntity(CommentRequest newCommentRequest);

    @Mapping(source = ".", target = "author", qualifiedByName = "toAuthor")
    void mapToExistingEntity(CommentRequest newCommentRequest, @MappingTarget Comment existingComment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = ".", target = "author", qualifiedByName = "toAuthor")
    void partialMapToExistingEntity(CommentRequest newCommentRequest, @MappingTarget Comment existingComment);


    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "author.username", target = "authorUsername")
    ShortCommentResponse mapToDTO(Comment comment);


    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "author.username", target = "authorUsername")
    CommentFullResponse mapToFullResponse(Comment comment);

    @Named("toAuthor")
    default Author toAuthor(CommentRequest commentRequest) {
        Author author = new Author();
        author.setId(commentRequest.getUserId());
        author.setUsername(commentRequest.getUsername());
        return author;
    }


}
