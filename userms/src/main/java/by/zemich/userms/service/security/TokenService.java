package by.zemich.userms.service.security;

import by.zemich.userms.security.properties.JWTProperty;
import by.zemich.userms.dao.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final JWTProperty jwtProperty;

    @Value("${spring.application.name}")
    private String applicationName;

    public String generate(User user) {
        SecretKey signingKey = Keys.hmacShaKeyFor(jwtProperty.getKey().getBytes(StandardCharsets.UTF_8));


        return Jwts.builder()
                .subject(user.getUsername())
                .claim("id", user.getId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(jwtProperty.getDuration())))
                .issuer(applicationName)
                .signWith(signingKey)
                .compact();
    }
}
