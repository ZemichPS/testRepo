package by.zemich.newsms.security.config;

import by.zemich.newsms.security.filter.JWTFilter;
import by.zemich.newsms.security.managers.CommenOwnerAuthorizationManager;
import by.zemich.newsms.security.managers.UserAdminOrUserOwnerAuthorizationManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            UserAdminOrUserOwnerAuthorizationManager userAdminOrUserOwnerAuthorizationManager,
            CommenOwnerAuthorizationManager commenOwnerAuthorizationManager,
            HttpSecurity http,
            JWTFilter jwtFilter
    ) throws Exception {
        return http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(
                                "/swagger-ui",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-ui/index.html",
                                "/v3/api-docs",
                                "/v3/api-docs/swagger-config"
                        ).permitAll()
                        .requestMatchers(antMatcher(HttpMethod.POST, "/api/v1/news")).hasAnyRole("ADMIN", "JOURNALIST")
                        .requestMatchers(
                                antMatcher(HttpMethod.PUT, "/api/v1/news/{newsId}"),
                                antMatcher(HttpMethod.PATCH, "/api/v1/news/{newsId}"),
                                antMatcher(HttpMethod.DELETE, "/api/v1/news/{newsId}")

                        ).access(userAdminOrUserOwnerAuthorizationManager)
                        .requestMatchers(
                                antMatcher(HttpMethod.GET, "/api/v1/news"),
                                antMatcher(HttpMethod.GET, "/api/v1/news/full"),
                                antMatcher(HttpMethod.GET, "/api/v1/news/{newsId}"),
                                antMatcher(HttpMethod.GET, "/api/v1/news/{news_id}/comments{comment_id}")
                        ).permitAll()
                        .requestMatchers(antMatcher(HttpMethod.POST, "/api/v1/comments")).hasRole("SUBSCRIBER")
                        .requestMatchers(
                                antMatcher(HttpMethod.PUT, "/api/v1/comments/{commentId}"),
                                antMatcher(HttpMethod.PATCH, "/api/v1/comments/{commentId}"),
                                antMatcher(HttpMethod.DELETE, "/api/v1/comments/{commentId}")
                        ).access(commenOwnerAuthorizationManager)
                        .requestMatchers(antMatcher(HttpMethod.GET, "/api/v1/comments/{commentId}")).permitAll()
                        .anyRequest().authenticated())
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

//
//    @Bean
//    public SecurityFilterChain securityFilterChain(
//            HttpSecurity http
//    ) throws Exception {
//        return http
//                .authorizeHttpRequests((authorize) -> authorize
//                        .anyRequest().permitAll())
//                .csrf().disable()
//                .formLogin().disable()
//                .httpBasic().disable()
//                .build();
//    }


}

