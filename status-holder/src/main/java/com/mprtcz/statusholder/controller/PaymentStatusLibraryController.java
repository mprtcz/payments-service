package com.mprtcz.statusholder.controller;


import com.mprtcz.statusholder.dto.PaymentStatus;
import com.mprtcz.statusholder.service.PaymentStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentStatusLibraryController implements PaymentStatusController {
    private final PaymentStatusService paymentStatusService;

    @Override
    public String createPaymentStatus() {
        return paymentStatusService.createPaymentStatus();
    }

    @Override
    public PaymentStatus getPaymentStatus(String paymentId) {
        return paymentStatusService.getPaymentStatus(paymentId);
    }

    @Override
    public void markTransactionAsInvalid(String id) {
        paymentStatusService.markTransactionAsInvalid(id);
    }
}
