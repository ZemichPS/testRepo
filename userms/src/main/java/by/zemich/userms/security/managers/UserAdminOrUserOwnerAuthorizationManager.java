package by.zemich.userms.security.managers;

import by.zemich.userms.dao.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class UserAdminOrUserOwnerAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final UserRepository userRepository;


    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {

        Map<String, String> pathVariables = context.getVariables();
        UUID userId = UUID.fromString(pathVariables.get("userId"));


        boolean isAdminOrSystemRole = authentication.get().getAuthorities()
                .stream()
                .anyMatch(role ->
                        role.equals(new SimpleGrantedAuthority("ROLE_ADMIN")) || role.equals(new SimpleGrantedAuthority("ROLE_SYSTEM")));
        if (isAdminOrSystemRole) {
            return new AuthorizationDecision(true);
        }

        String authenticationUsername = authentication.get().getName();
        boolean isAuthorized = userRepository.findById(userId)
                .orElseThrow(() -> new AccessDeniedException("Current user cannot get access to this point")).getUsername()
                .equals(authenticationUsername);

        if (isAuthorized) {
            return new AuthorizationDecision(true);
        }
        return new AuthorizationDecision(false);
    }

}
