package com.mprtcz.transaction.service;

import com.mprtcz.initiator.controllers.dto.PaymentRequest;
import com.mprtcz.transaction.repository.PaymentTransactionRepository;
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
