package com.mprtcz.repository;

import com.mprtcz.dto.PaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PaymentStatusRepository {

    private final Jedis jedis;

    public String createPaymentStatus(String id, String status) {
        jedis.set(id, status);
        return id;
    }

    public String getPaymentStatus(String paymentId) {
        return jedis.get(paymentId);
    }

    public void setStatusForTransaction(String id,
            PaymentStatus paymentStatus) {
        var oldStatus = jedis.get(id);

        if (oldStatus == null) {
            throw new IllegalStateException("Payment status not set");
        }
        jedis.set(id, paymentStatus.getStatus());
    }
}
