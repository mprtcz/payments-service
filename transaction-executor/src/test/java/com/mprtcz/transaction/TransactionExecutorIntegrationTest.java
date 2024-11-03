package com.mprtcz.transaction;

import com.mprtcz.transaction.controller.PaymentTransactionController;
import com.mprtcz.transaction.dto.TransactionRequest;
import com.mprtcz.transaction.publishers.PublisherQueue;
import com.mprtcz.transaction.repository.PaymentTransactionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ContextConfiguration(classes = IntegrationTestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = "/data.sql", executionPhase =
        Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class TransactionExecutorIntegrationTest {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    private PublisherQueue<TransactionRequest> publisherQueue;
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
        var paymentMessage = new TransactionRequest(requesterAccountNumber,
                destinationAccountNumber,
                BigDecimal.valueOf(200));
        publisherQueue.publish(paymentMessage);

        paymentTransactionController.processQueueMessages();

        var destinationAccount =
                paymentTransactionRepository.findByAccountNumber(
                destinationAccountNumber);
        var requesterAccount = paymentTransactionRepository.findByAccountNumber(
                requesterAccountNumber);

        assertEquals(destinationAccount.get().balance().intValue(), 700);
        assertEquals(requesterAccount.get().balance().intValue(), 800);
    }

    @Test
    void shouldNotProcessTransaction_insufficientFunds() {
        var requesterAccountNumber = "12345678901234567";
        var destinationAccountNumber = "123456789012345679";
        var paymentMessage = new TransactionRequest(requesterAccountNumber,
                destinationAccountNumber,
                BigDecimal.valueOf(200));
        publisherQueue.publish(paymentMessage);

        var ex = assertThrows(IllegalArgumentException.class,
                () -> paymentTransactionController.processQueueMessages());

        assertEquals("Insufficient funds", ex.getMessage());
    }

    @Test
    void shouldNotProcessTransaction_missingRequesterAccount() {
        var requesterAccountNumber = "12345678901234566";
        var destinationAccountNumber = "123456789012345679";
        var paymentMessage = new TransactionRequest(requesterAccountNumber,
                destinationAccountNumber,
                BigDecimal.valueOf(200));
        publisherQueue.publish(paymentMessage);

        var ex = assertThrows(IllegalArgumentException.class,
                () -> paymentTransactionController.processQueueMessages());

        assertEquals("Account not found", ex.getMessage());
    }

    @Test
    void shouldNotProcessTransaction_missingDestinationAccount() {
        var requesterAccountNumber = "123456789012345678";
        var destinationAccountNumber = "123456789012345670";

        var requesterAccoun2 = paymentTransactionRepository.findByAccountNumber(
                requesterAccountNumber);
        assertEquals(1000, requesterAccoun2.get().balance().intValue());
        var paymentMessage = new TransactionRequest(requesterAccountNumber,
                destinationAccountNumber,
                BigDecimal.valueOf(200));
        publisherQueue.publish(paymentMessage);

        var ex = assertThrows(IllegalStateException.class,
                () -> paymentTransactionController.processQueueMessages());

        assertEquals("Failed to credit the amount", ex.getMessage());
        // Should rollback the requester subtraction
        var requesterAccount = paymentTransactionRepository.findByAccountNumber(
                requesterAccountNumber);
        assertEquals(1000, requesterAccount.get().balance().intValue());
    }
}
