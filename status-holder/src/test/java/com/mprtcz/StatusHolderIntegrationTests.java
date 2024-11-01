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

@SpringJUnitConfig
@ContextConfiguration(classes = {IntegrationTestConfig.class})
public class StatusHolderIntegrationTests {

    @Autowired
    private PaymentStatusController paymentStatusController;
    @Autowired
    private Jedis jedis;

    @BeforeEach
    public void setUp() {
        jedis.flushDB();
    }

    @Test
    public void statusHolderIntegrationTests_insertsStatusSuccessfully() {
        String paymentId = paymentStatusController.createPaymentStatus();

        assertEquals(PaymentStatus.STARTED.getStatus(), jedis.get(paymentId));
    }

    @Test
    public void statusHolderIntegrationTests_returnMissingForNotPresentStatus() {
        var status = paymentStatusController.getPaymentStatus("missing_payment_status");
        assertEquals(PaymentStatus.MISSING, status);
    }
}
