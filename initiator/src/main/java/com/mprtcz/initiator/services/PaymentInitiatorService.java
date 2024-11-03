package com.mprtcz.initiator.services;

import com.mprtcz.statusholder.controller.PaymentStatusController;
import com.mprtcz.initiator.controllers.dto.PaymentRequest;
import com.mprtcz.initiator.publishers.PublisherQueue;
import com.mprtcz.validator.controller.ValidationController;
import com.mprtcz.statusholder.dto.PaymentStatus;
import com.mprtcz.initiator.exceptions.TransactionInvalidException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentInitiatorService {
    private final PaymentStatusController paymentStatusController;
    private final ValidationController validationController;
    private final PublisherQueue<PaymentRequest> publisherQueue;

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
        publisherQueue.publish(paymentRequest);
        return id;
    }

    public PaymentStatus getPaymentStatus(String id) {
        return paymentStatusController.getPaymentStatus(id);
    }
}
