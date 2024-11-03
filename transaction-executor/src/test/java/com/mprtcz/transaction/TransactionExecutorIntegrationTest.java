package com.mprtcz.transaction;

import com.github.tomakehurst.wiremock.admin.NotFoundException;
import com.mprtcz.initiator.controllers.dto.PaymentRequest;
import com.mprtcz.initiator.publishers.PublisherQueue;
import com.mprtcz.initiator.publishers.QueueMessage;
import com.mprtcz.transaction.controller.PaymentTransactionController;
import com.mprtcz.transaction.repository.PaymentTransactionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigDecimal;
import java.time.Clock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
@ContextConfiguration(classes = IntegrationTestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = "/data.sql", executionPhase =
        Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class TransactionExecutorIntegrationTest {

    Clock clock = mock(Clock.class);
    @Autowired
    JdbcTemplate jdbcTemplate;
    @MockBean
    private PublisherQueue<PaymentRequest> publisherQueue;
    @Autowired
    private PaymentTransactionController paymentTransactionController;
    @Autowired
    private PaymentTransactionRepository paymentTransactionRepository;

    @AfterEach
    void cleanUp() {
        jdbcTemplate.execute("TRUNCATE TABLE accounts;");
    }

    @Test
    void shouldProcessTransactionSuccessfully() {
        var requesterAccountNumber = "123456789012345678";
        var destinationAccountNumber = "123456789012345679";
        var paymentMessage = new PaymentRequest(requesterAccountNumber,
                destinationAccountNumber,
                BigDecimal.valueOf(200));
        when(publisherQueue.take()).thenReturn(new QueueMessage<>(paymentMessage,
                clock), null);

        paymentTransactionController.processQueueMessages();

        var destinationAccount =
                paymentTransactionRepository.findByAccountNumber(
                destinationAccountNumber);
        var requesterAccount = paymentTransactionRepository.findByAccountNumber(
                requesterAccountNumber);

        assertEquals(destinationAccount.get().balance().intValue(), 700);
        assertEquals(requesterAccount.get().balance().intValue(), 800);
        verify(publisherQueue).acknowledge(any());
    }

    @Test
    void shouldNotProcessTransaction_insufficientFunds() {
        var requesterAccountNumber = "12345678901234567";
        var destinationAccountNumber = "123456789012345679";
        var paymentMessage = new PaymentRequest(requesterAccountNumber,
                destinationAccountNumber,
                BigDecimal.valueOf(200));
        when(publisherQueue.take()).thenReturn(new QueueMessage<>(paymentMessage,
                clock), null);

        var ex = assertThrows(IllegalArgumentException.class,
                () -> paymentTransactionController.processQueueMessages());

        assertEquals("Insufficient funds", ex.getMessage());
        verify(publisherQueue, never()).acknowledge(any());
    }

    @Test
    void shouldNotProcessTransaction_missingRequesterAccount() {
        var requesterAccountNumber = "12345678901234566";
        var destinationAccountNumber = "123456789012345679";
        var paymentMessage = new PaymentRequest(requesterAccountNumber,
                destinationAccountNumber,
                BigDecimal.valueOf(200));
        when(publisherQueue.take()).thenReturn(new QueueMessage<>(paymentMessage,
                clock), null);

        var ex = assertThrows(NotFoundException.class,
                () -> paymentTransactionController.processQueueMessages());

        assertEquals("Account not found", ex.getMessage());
        verify(publisherQueue, never()).acknowledge(any());
    }

    @Test
    void shouldNotProcessTransaction_missingDestinationAccount() {
        var requesterAccountNumber = "123456789012345678";
        var destinationAccountNumber = "123456789012345670";

        var requesterAccoun2 = paymentTransactionRepository.findByAccountNumber(
                requesterAccountNumber);
        assertEquals(1000, requesterAccoun2.get().balance().intValue());
        var paymentMessage = new PaymentRequest(requesterAccountNumber,
                destinationAccountNumber,
                BigDecimal.valueOf(200));
        when(publisherQueue.take()).thenReturn(new QueueMessage<>(paymentMessage,
                clock), null);

        var ex = assertThrows(IllegalStateException.class,
                () -> paymentTransactionController.processQueueMessages());

        assertEquals("Failed to credit the amount", ex.getMessage());
        verify(publisherQueue, never()).acknowledge(any());
        // Should rollback the requester subtraction
        var requesterAccount = paymentTransactionRepository.findByAccountNumber(
                requesterAccountNumber);
        assertEquals(1000, requesterAccount.get().balance().intValue());
    }
}
