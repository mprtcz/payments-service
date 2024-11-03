package com.mprtcz.statusholder.controller;

import com.mprtcz.statusholder.dto.PaymentStatus;
import org.springframework.stereotype.Component;

@Component
public interface PaymentStatusController {
    String createPaymentStatus();
    PaymentStatus getPaymentStatus(String paymentId);
    void markTransactionAsInvalid(String id);
    void markTransactionAsSuccessful(String id);
    void markTransactionAsFailed(String id);
}
