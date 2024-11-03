package com.mprtcz.statusholder.service;

import com.mprtcz.statusholder.dto.PaymentStatus;
import com.mprtcz.statusholder.repository.PaymentStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PaymentStatusService {
    private final PaymentStatusRepository paymentStatusRepository;

    public String createPaymentStatus() {
        return paymentStatusRepository.createPaymentStatus(UUID.randomUUID().toString(),
            PaymentStatus.STARTED.getStatus()
        );
    }

    public PaymentStatus getPaymentStatus(String paymentId) {
        return PaymentStatus.fromStatus(paymentStatusRepository.getPaymentStatus(paymentId));
    }

    public void markTransactionAsInvalid(String id) {
        paymentStatusRepository.setStatusForTransaction(id,
                PaymentStatus.INVALID);
    }

    public void markTransactionAsSuccessful(String id) {
        paymentStatusRepository.setStatusForTransaction(id,
                PaymentStatus.SUCCESS);
    }

    public void markTransactionAsFailed(String id) {
        paymentStatusRepository.setStatusForTransaction(id,
                PaymentStatus.FAILED);
    }
}
