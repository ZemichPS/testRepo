package by.zemich.userms.security.config;

import by.zemich.userms.dao.repository.UserRepository;
import by.zemich.userms.security.filters.JWTFilter;
import by.zemich.userms.security.properties.JWTProperty;
import by.zemich.userms.security.managers.UserAdminOrUserOwnerAuthorizationManager;
import by.zemich.userms.security.utils.JWTHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JWTFilter jwtFilter,
            AuthorizationManager<RequestAuthorizationContext> userAdminOrAddressOwnerAuthorizationManager
    ) throws Exception {
        return http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(antMatcher(HttpMethod.GET, "/api/v1/users"),
                                antMatcher(HttpMethod.PATCH, "/api/v1/users/{userId}/deactivation"),
                                antMatcher(HttpMethod.PATCH, "/api/v1/users/{userId}/activation")).hasRole("ADMIN")
                        .requestMatchers(antMatcher(HttpMethod.GET, "/api/v1/users/{userId}"))
                        .access(userAdminOrAddressOwnerAuthorizationManager)
                        .requestMatchers(antMatcher(HttpMethod.POST)).permitAll()
                        .requestMatchers(antMatcher(HttpMethod.GET, "/api/v1/auth/login")).permitAll())
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService detailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(passwordEncoder);
        authenticationProvider.setUserDetailsService(detailsService);
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationProvider authenticationProvider) {
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public JWTFilter jwtFilter(JWTHandler jwtHandler, UserDetailsService userDetailsService) {
        return new JWTFilter(jwtHandler, userDetailsService);
    }

    @Bean
    public JWTHandler jwtHandler(JWTProperty jwtProperty) {
        return new JWTHandler(jwtProperty);
    }

    @Bean
    public AuthorizationManager<RequestAuthorizationContext> userAdminOrAddressOwnerAuthorizationManager(UserRepository userRepository) {
        return new UserAdminOrUserOwnerAuthorizationManager(userRepository);
    }
}
