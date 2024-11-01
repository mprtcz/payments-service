package com.mprtcz.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

@Repository
@RequiredArgsConstructor
public class PaymentStatusRepository {

    private final Jedis jedis;

    public String createPaymentStatus(String id, String status) {
        jedis.set(id, status);
        return id;
    }

    public String getPaymentStatus(String paymentId) {
        return jedis.get(paymentId);
    }
}
