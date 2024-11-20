package by.zemich.newsms.security.utils;

import by.zemich.newsms.security.properties.JWTProperty;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;


@Component
@RequiredArgsConstructor
public class JWTHandler {
    private final JWTProperty jwtProperty;
    private SecretKey secretKey;
    @Value("${spring.application.name}")
    private String issuer;

    public String getUserName(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token).getPayload();
        return claims.getSubject();
    }

    public String getUserId(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token).getPayload();
        return claims.get("id", String.class);
    }

    public boolean validate(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            throw new IllegalArgumentException("Invalid JWT signature", e);
        }
    }

    public String generateSystem() {
        SecretKey signingKey = Keys.hmacShaKeyFor(jwtProperty.getKey().getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .subject("SYSTEM")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(30)))
                .issuer(issuer)
                .signWith(signingKey)
                .compact();
    }

    @PostConstruct
    private void init(){
        secretKey = Keys.hmacShaKeyFor(jwtProperty.getKey().getBytes(StandardCharsets.UTF_8));
    }
}
