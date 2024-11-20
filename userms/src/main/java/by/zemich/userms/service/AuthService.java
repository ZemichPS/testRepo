package by.zemich.userms.service;

import by.zemich.userms.dao.entity.User;
import by.zemich.userms.service.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRestService userRestService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public String authenticate(String login, String password) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                login, password
        );
        Authentication authenticationResult = authenticationManager.authenticate(authentication);
        if (!authenticationResult.isAuthenticated()) {
            throw new BadCredentialsException("Bad credentials");
        }
        return getToken(login);
    }


    public String changePassword(String login, String oldPassword, String newPassword) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                login, oldPassword
        );
        Authentication authenticationResult = authenticationManager.authenticate(authentication);
        if (!authenticationResult.isAuthenticated()) {
            throw new BadCredentialsException("Bad credentials");
        }
        userRestService.changePassword(login, newPassword);
        return getToken(login);
    }


    private String getToken(String userName) {
        User user = userRestService.findByUsername(userName).orElseThrow(() -> new UsernameNotFoundException(
                "User with email %s is nowhere to be found".formatted(userName))
        );
        return tokenService.generate(user);
    }


}
