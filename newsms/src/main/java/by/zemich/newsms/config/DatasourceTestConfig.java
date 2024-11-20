package by.zemich.newsms.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
public class DatasourceTestConfig {
    @Bean
    @Profile("test")
    DataSource dataSource(){
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/news_test_db");
        config.setUsername("postgres");
        config.setPassword("postgres");
        config.setSchema("app");
        return new HikariDataSource(config);
    }
}
