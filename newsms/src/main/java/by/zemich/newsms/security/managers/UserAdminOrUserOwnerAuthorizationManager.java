package by.zemich.newsms.security.managers;

import by.zemich.newsms.api.dao.NewsRepository;
import by.zemich.newsms.core.domain.News;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
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
public class UserAdminOrUserOwnerAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final NewsRepository newsRepository;


    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {

        boolean isAdmin = authentication.get().getAuthorities()
                .stream()
                .anyMatch(role ->
                        role.equals(new SimpleGrantedAuthority("ROLE_ADMIN")) || role.equals(new SimpleGrantedAuthority("ROLE_SYSTEM")));

        if (isAdmin) {
            return new AuthorizationDecision(true);
        }


        Map<String, String> pathVariables = context.getVariables();
        UUID newsId = UUID.fromString(pathVariables.get("newsId"));
        News news = newsRepository.findById(newsId).orElseThrow();

        UserDetails userDetails = (UserDetails) authentication.get().getDetails();
        String username = userDetails.getUsername();
        boolean isOwner = username.equals(news.getAuthor().getUsername());

        if (isOwner) {
            return new AuthorizationDecision(true);
        }

        return new AuthorizationDecision(false);
    }



}
