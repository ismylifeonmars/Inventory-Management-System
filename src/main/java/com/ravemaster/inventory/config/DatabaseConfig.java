package com.ravemaster.inventory.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {
    private final Environment env;

    public DatabaseConfig(Environment env) {
        this.env = env;
    }

    @Bean
    @Primary
    public DataSource dataSource(){
        String host = env.getProperty("DB_HOST");
        Integer port = Integer.valueOf(env.getProperty("DB_PORT", "5432"));  // Default to 5432
        String database = env.getProperty("DB_DATABASE");
        String username = env.getProperty("SPRING_DATASOURCE_USERNAME");
        String password = env.getProperty("SPRING_DATASOURCE_PASSWORD");

        if (host == null || database == null) {
            throw new IllegalStateException("Missing required DB properties: DB_HOST or DB_DATABASE");
        }

        String jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s", host, port, database);

        return DataSourceBuilder.create()
                .url(jdbcUrl)
                .username(username)
                .password(password)
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}
