package com.mprtcz;

import com.mprtcz.controller.PaymentTransactionController;
import com.mprtcz.controllers.dto.PaymentRequest;
import com.mprtcz.publishers.PublisherQueue;
import com.mprtcz.publishers.QueueMessage;
import com.mprtcz.repository.PaymentTransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigDecimal;
import java.time.Clock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
@ContextConfiguration(classes = IntegrationTestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TransactionExecutorIntegrationTest {

    Clock clock = mock(Clock.class);
    @MockBean
    private PublisherQueue<PaymentRequest> publisherQueue;
    @Autowired
    private PaymentTransactionController paymentTransactionController;
    @Autowired
    private PaymentTransactionRepository paymentTransactionRepository;

    @Test
    void shouldProcessTransactionSuccessfully() {
        var requesterAccountNumber = "123456789012345678";
        var destinationAccountNumber = "123456789012345679";
        var paymentMessage = new PaymentRequest(requesterAccountNumber,
                destinationAccountNumber,
                BigDecimal.valueOf(200));
        when(publisherQueue.take()).thenReturn(new QueueMessage<>(paymentMessage,
                clock));

        paymentTransactionController.processQueueMessages();

        var destinationAccount =
                paymentTransactionRepository.findByAccountNumber(
                destinationAccountNumber);
        var requesterAccount = paymentTransactionRepository.findByAccountNumber(
                requesterAccountNumber);

        assertEquals(destinationAccount.get().balance().intValue(), 1200);
        assertEquals(requesterAccount.get().balance().intValue(), 300);
    }


}
