package com.com.pri_vrat.authentication.service;

import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@RequiredArgsConstructor
@Service
public class FlywayMigrationServiceImplementation implements FlywayMigrationService {

    private final DataSource dataSource;

    @Override
    public void tenantMigration(String tenant) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .schemas(tenant)
                .defaultSchema(tenant)
                .locations("classpath:db/migration/tenant")
                .createSchemas(true)
                .load();
        flyway.migrate();
    }
}
