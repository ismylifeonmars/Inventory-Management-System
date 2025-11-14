package com.ravemaster.inventory.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
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
    public DataSource dataSource() {
        String activeProfile = env.getActiveProfiles().length > 0
                ? env.getActiveProfiles()[0]
                : "dev";

        return switch (activeProfile) {
            case "prod" -> prodDataSource();
            case "dev"  -> devDataSource();
            case "test" -> testDataSource();
            default     -> throw new IllegalStateException("Unknown profile: " + activeProfile);
        };
    }

    private DataSource prodDataSource() {
        String host     = env.getRequiredProperty("DB_HOST");
        String port     = env.getProperty("DB_PORT", "5432");
        String db       = env.getRequiredProperty("DB_DATABASE");
        String user     = env.getRequiredProperty("SPRING_DATASOURCE_USERNAME");
        String pass     = env.getRequiredProperty("SPRING_DATASOURCE_PASSWORD");
        String driver   = env.getProperty("SPRING_DATASOURCE_DRIVER_CLASS_NAME", "org.postgresql.Driver");

        String url = "jdbc:postgresql://%s:%s/%s".formatted(host, port, db);
        return DataSourceBuilder.create()
                .url(url).username(user).password(pass).driverClassName(driver)
                .build();
    }

    private DataSource devDataSource() {
        String host   = env.getProperty("DEV_DB_HOST", "localhost");
        String port   = env.getProperty("DEV_DB_PORT", "5432");
        String db     = env.getProperty("DEV_DB_NAME", "postgres");
        String user   = env.getProperty("DEV_DB_USER", "postgres");
        String pass   = env.getProperty("DEV_DB_PASS", "changemeinprod!");
        String driver = "org.postgresql.Driver";

        String url = "jdbc:postgresql://%s:%s/%s".formatted(host, port, db);
        return DataSourceBuilder.create()
                .url(url).username(user).password(pass).driverClassName(driver)
                .build();
    }

    private DataSource testDataSource() {
        String dbName = env.getProperty("TEST_DB_NAME", "testdb");
        String url    = "jdbc:h2:mem:%s;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH"
                .formatted(dbName);
        return DataSourceBuilder.create()
                .url(url)
                .username("sa")
                .password("")
                .driverClassName("org.h2.Driver")
                .build();
    }

}
