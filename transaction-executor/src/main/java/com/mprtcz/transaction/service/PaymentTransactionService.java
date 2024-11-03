package com.mprtcz.transaction.service;

import com.mprtcz.statusholder.controller.PaymentStatusController;
import com.mprtcz.transaction.dto.TransactionRequest;
import com.mprtcz.transaction.repository.PaymentTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
@RequiredArgsConstructor
public class PaymentTransactionService {
    private final PaymentTransactionRepository transactionRepository;
    private final PaymentStatusController paymentStatusController;

    public void processTransaction(TransactionRequest messageContent) {
        transactionRepository.transfer(messageContent.paymentRequesterAccountNumber(),
                messageContent.paymentDestinationAccountNumber(),
                messageContent.amount());
        paymentStatusController.markTransactionAsSuccessful(messageContent.id());
    }
}
