package com.mprtcz;

import com.mprtcz.controller.PaymentStatusController;
import com.mprtcz.dto.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import redis.clients.jedis.Jedis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringJUnitConfig
@ContextConfiguration(classes = {IntegrationTestConfig.class})
class StatusHolderIntegrationTests {

    @Autowired
    private PaymentStatusController paymentStatusController;
    @Autowired
    private Jedis jedis;

    @BeforeEach
    public void setUp() {
        jedis.flushDB();
    }

    @Test
    void insertsStatusSuccessfully() {
        String paymentId = paymentStatusController.createPaymentStatus();

        assertEquals(PaymentStatus.STARTED.getStatus(), jedis.get(paymentId));
    }

    @Test
    void returnMissingForNotPresentStatus() {
        var status = paymentStatusController.getPaymentStatus(
                "missing_payment_status");
        assertEquals(PaymentStatus.MISSING, status);
    }

    @Test
    void updateStatus_successful() {
        String paymentId = paymentStatusController.createPaymentStatus();

        paymentStatusController.markTransactionAsInvalid(paymentId);

        assertEquals(PaymentStatus.INVALID.getStatus(), jedis.get(paymentId));
    }

    @Test
    void updateStatus_previousStatusMissing() {
        var ex = assertThrows(IllegalStateException.class,
                () -> paymentStatusController.markTransactionAsInvalid(
                        "missing_payment_status"));

        assertEquals("Payment status not set", ex.getMessage());
    }
}
