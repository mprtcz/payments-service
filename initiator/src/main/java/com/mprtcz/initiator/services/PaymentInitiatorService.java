package com.mprtcz.initiator.services;

import com.mprtcz.statusholder.controller.PaymentStatusController;
import com.mprtcz.initiator.controllers.dto.PaymentRequest;
import com.mprtcz.transaction.dto.TransactionRequest;
import com.mprtcz.transaction.publishers.PublisherQueue;
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
    private final PublisherQueue<TransactionRequest> publisherQueue;

    public String processPayment(PaymentRequest paymentRequest) {
        var id = paymentStatusController.createPaymentStatus();
        var validationResult = validationController.validate(paymentRequest.getPaymentRequesterAccountNumber(), paymentRequest.getPaymentDestinationAccountNumber());
        if (!validationResult) {
            paymentStatusController.markTransactionAsInvalid(id);
            throw new TransactionInvalidException();
        }
        publisherQueue.publish(
                new TransactionRequest(paymentRequest.getPaymentRequesterAccountNumber(),
                        paymentRequest.getPaymentDestinationAccountNumber(),
                        paymentRequest.getAmount(),
                        id));
        return id;
    }

    public PaymentStatus getPaymentStatus(String id) {
        return paymentStatusController.getPaymentStatus(id);
    }
}
