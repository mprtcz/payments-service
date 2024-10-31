package com.mprtcz.controllers.services;

import com.mprtcz.controllers.dto.PaymentRequest;
import org.springframework.stereotype.Service;

@Service
public class PaymentInitiatorService {

    public void processPayment(PaymentRequest paymentRequest) {
        // Connect to dynamo modul to get uuid of the transaction
        // Perform user security validations
        // Send a message to queue to validate transaction
    }
}
