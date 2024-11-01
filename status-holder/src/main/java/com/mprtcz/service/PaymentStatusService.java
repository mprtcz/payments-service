package com.mprtcz.service;

import com.mprtcz.dto.PaymentStatus;
import com.mprtcz.repository.PaymentStatusRepository;
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
}
