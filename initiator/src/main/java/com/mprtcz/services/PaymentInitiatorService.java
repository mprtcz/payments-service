package com.mprtcz.services;

import com.mprtcz.controller.PaymentStatusController;
import com.mprtcz.controllers.dto.PaymentRequest;
import com.mprtcz.dto.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentInitiatorService {
    private final PaymentStatusController paymentStatusController;

    public String processPayment(PaymentRequest paymentRequest) {
        // Connect to dynamo modul to get uuid of the transaction
        var id = paymentStatusController.createPaymentStatus();
        // Perform user security validations
        // Send a message to queue to validate transaction
        return id;
    }

    public PaymentStatus getPaymentStatus(String id) {
        return paymentStatusController.getPaymentStatus(id);
    }
}
