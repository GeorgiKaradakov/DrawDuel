package com.drawduel.tests.config;

import javax.sql.DataSource;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@TestConfiguration
public class TestDatabaseConfig {

  @Bean
  @Primary
  public DataSource dataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName("org.postgresql.Driver");
    dataSource.setUrl(System.getProperty("spring.datasource.url"));
    dataSource.setUsername(System.getProperty("spring.datasource.username"));
    dataSource.setPassword(System.getProperty("spring.datasource.password"));
    return dataSource;
  }
}
