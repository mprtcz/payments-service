package com.mprtcz;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = "com.mprtcz")
@ActiveProfiles("it")
public class IntegrationTestConfig {

    @Bean
    public DataSource dataSource() {
        return new org.h2.jdbcx.JdbcDataSource(); // Configure H2 database
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
