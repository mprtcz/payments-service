package com.mprtcz.initiator.controllers.dto;


import com.mprtcz.statusholder.dto.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PaymentStatusResponse {
    private String paymentStatus;

    public static PaymentStatusResponse create(PaymentStatus paymentStatus) {
        return new PaymentStatusResponse(paymentStatus.getStatus());
    }
}
