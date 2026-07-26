package com.com.pri_vrat.authentication.config;

import com.com.pri_vrat.authentication.util.Constants;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {

    @Value("${spring.datasource.url}")
    private String dataSourceUri;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean(initMethod = "migrate")
    public Flyway configDefault() {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSourceUri, username, password)
                .schemas(Constants.SCHEMA.DEFAULT_SCHEMA)
                .locations("classpath:db/migration/public")
                .defaultSchema(Constants.SCHEMA.DEFAULT_SCHEMA)
                .baselineOnMigrate(true)
                .load();
        flyway.migrate();
        return flyway;
    }

}
