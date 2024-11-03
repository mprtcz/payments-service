package com.mprtcz.transaction;

import com.mprtcz.statusholder.controller.PaymentStatusController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan(basePackages = "com.mprtcz.transaction")
@ActiveProfiles("it")
@EnableTransactionManagement
public class IntegrationTestConfig {

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public PaymentStatusController getPaymentStatusController() {
        return mock(PaymentStatusController.class);
    }
}
