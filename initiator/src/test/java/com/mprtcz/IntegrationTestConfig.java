package com.mprtcz;


import com.mprtcz.controller.PaymentStatusController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan(basePackages = "com.mprtcz")
@ActiveProfiles("it")
public class IntegrationTestConfig {

    @Bean
    @Primary
    public PaymentStatusController getPaymentStatusController() {
        return mock(PaymentStatusController.class);
    }
}
