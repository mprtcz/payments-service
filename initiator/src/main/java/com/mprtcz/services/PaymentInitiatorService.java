package com.mprtcz.services;

import com.mprtcz.controller.PaymentStatusController;
import com.mprtcz.controller.ValidationController;
import com.mprtcz.controllers.dto.PaymentRequest;
import com.mprtcz.dto.PaymentStatus;
import com.mprtcz.exceptions.TransactionInvalidException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentInitiatorService {
    private final PaymentStatusController paymentStatusController;
    private final ValidationController validationController;

    public String processPayment(PaymentRequest paymentRequest) {
        // Connect to redis module to get uuid of the transaction
        var id = paymentStatusController.createPaymentStatus();
        // Perform user security validations
        var validationResult = validationController.validate(paymentRequest.getPaymentRequesterAccountNumber(), paymentRequest.getPaymentDestinationAccountNumber());
        if (!validationResult) {
            paymentStatusController.markTransactionAsInvalid(id);
            throw new TransactionInvalidException();
        }
        // Send a message to queue to validate transaction
        return id;
    }

    public PaymentStatus getPaymentStatus(String id) {
        return paymentStatusController.getPaymentStatus(id);
    }
}
