package by.zemich.userms.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
@Data
public class JWTProperty {
    private String key;
    private int duration;
}
