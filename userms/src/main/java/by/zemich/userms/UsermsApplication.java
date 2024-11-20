package by.zemich.userms;

import by.zemich.userms.security.properties.JWTProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties({JWTProperty.class})
public class UsermsApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsermsApplication.class, args);
    }

}

