package com.mprtcz.initiator;


import com.mprtcz.statusholder.controller.PaymentStatusController;
import com.mprtcz.transaction.dto.Identifiable;
import com.mprtcz.transaction.dto.TransactionRequest;
import com.mprtcz.transaction.publishers.PublisherQueue;
import com.mprtcz.validator.controller.ValidationController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan(basePackages = "com.mprtcz.initiator")
@ActiveProfiles("it")
public class IntegrationTestConfig {

    @Bean
    @Primary
    public PublisherQueue<TransactionRequest> getPublisherQueue() {
        return mock(PublisherQueue.class);
    }

    @Bean
    @Primary
    public PaymentStatusController getPaymentStatusController() {
        return mock(PaymentStatusController.class);
    }

    @Bean
    @Primary
    public ValidationController getValidationController() {
        return mock(ValidationController.class);
    }
}
