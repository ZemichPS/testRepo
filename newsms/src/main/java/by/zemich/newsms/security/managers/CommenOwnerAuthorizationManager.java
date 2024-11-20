package by.zemich.newsms.security.managers;

import by.zemich.newsms.api.dao.CommentRepository;
import by.zemich.newsms.core.domain.Comment;
import by.zemich.newsms.core.domain.News;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class CommenOwnerAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final CommentRepository commentRepository;


    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {

        Map<String, String> pathVariables = context.getVariables();
        UUID commentId = UUID.fromString(pathVariables.get("commentId"));
        Comment comment = commentRepository.findById(commentId).orElseThrow();

        UserDetails userDetails = (UserDetails) authentication.get().getDetails();
        String username = userDetails.getUsername();
        boolean isOwner = username.equals(comment.getAuthor().getUsername());
        if (isOwner) {
            return new AuthorizationDecision(true);
        }
        return new AuthorizationDecision(false);
    }


}
