package by.zemich.newsms.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
public class DatasourceProdConfig {
    @Bean
    @Profile("prod")
    DataSource dataSource(){
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl("jdbc:postgresql://news-db:5433/news");
        config.setUsername("postgres");
        config.setPassword("postgres");
        config.setSchema("app");
        return new HikariDataSource(config);
    }
}
