package by.zemich.userms.security.utils;

import by.zemich.userms.security.properties.JWTProperty;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;


@RequiredArgsConstructor
public class JWTHandler {
    private final JWTProperty jwtProperty;
    private SecretKey secretKey;

    public String getUserName(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token).getPayload();
        return claims.getSubject();
    }

    public String getUserIssuer(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token).getPayload();
        return claims.getIssuer();
    }

    public boolean validate(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            throw new IllegalArgumentException("Invalid JWT signature", e);
        }
    }

    @PostConstruct
    private void init(){
        secretKey = Keys.hmacShaKeyFor(jwtProperty.getKey().getBytes(StandardCharsets.UTF_8));
    }
}
