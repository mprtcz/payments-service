package com.mprtcz.service;

import com.mprtcz.controllers.dto.PaymentRequest;
import com.mprtcz.repository.PaymentTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
@RequiredArgsConstructor
public class PaymentTransactionService {
    private final PaymentTransactionRepository transactionRepository;

    public void processTransaction(PaymentRequest messageContent) {
        transactionRepository.transfer(messageContent.getPaymentRequesterAccountNumber(),
                messageContent.getPaymentDestinationAccountNumber(),
                messageContent.getAmount());
    }
}
