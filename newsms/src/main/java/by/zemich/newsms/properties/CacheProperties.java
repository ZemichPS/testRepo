package by.zemich.newsms.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cache")
@Data
public class CacheProperties {
    private boolean enabled;
    private int size;
    private String algorithm;
}
