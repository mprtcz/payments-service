package com.mprtcz.controller;

import com.mprtcz.dto.PaymentStatus;
import org.springframework.stereotype.Component;

@Component
public interface PaymentStatusController {
    String createPaymentStatus();
    PaymentStatus getPaymentStatus(String paymentId);
    void markTransactionAsInvalid(String id);
}
