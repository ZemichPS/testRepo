package by.zemich.newsms.security.service;

import by.zemich.newsms.security.utils.JWTHandler;
import by.zemich.newsms.core.domain.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserFeignClient userFeignClient;
    private final JWTHandler jwtHandler;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String accessToken = "Bearer " + jwtHandler.generateSystem();
        final UserFeignClient.UserFullResponse userByUsername = userFeignClient.getUserByUsername(username, accessToken);

        return User.builder()
                .username(userByUsername.getUsername())
                .roles(userByUsername.getRole())
                .password("")
                .build();
    }
}
