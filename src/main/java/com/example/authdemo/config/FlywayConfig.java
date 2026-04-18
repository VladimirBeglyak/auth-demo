package com.example.authdemo.config;

import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FlywayConfig {

  @Bean(initMethod = "migrate")
  public Flyway flyway(
      DataSource dataSource,
      @Value("${flyway.settings.schema}") String schema,
      @Value("${flyway.settings.location}") String location,
      @Value("${flyway.settings.baseLineOnMigrate}") boolean baseLineOnMigrate
  ) {
    return Flyway.configure()
        .dataSource(dataSource)
        .schemas(schema)
        .locations(location)
        .baselineOnMigrate(baseLineOnMigrate)
        .load();
  }
}