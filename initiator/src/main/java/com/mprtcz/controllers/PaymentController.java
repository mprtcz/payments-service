package com.mprtcz.controllers;

import com.mprtcz.controllers.dto.PaymentRequest;
import com.mprtcz.controllers.dto.PaymentResponse;
import com.mprtcz.dto.PaymentStatus;
import com.mprtcz.services.PaymentInitiatorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentInitiatorService paymentInitiatorService;

    @PostMapping("/v1/payment/initiate")
    public ResponseEntity<PaymentResponse> initiatePayment(@Valid @RequestBody PaymentRequest paymentRequest) {
        log.info("paymentRequest = {}", paymentRequest);
        var id = paymentInitiatorService.processPayment(paymentRequest);
        return ResponseEntity.accepted().body(new PaymentResponse(id));
    }

    @GetMapping("/v1/payment/status/{id}")
    public ResponseEntity<PaymentStatus> getPaymentStatus(@PathVariable("id") String id) {
        return ResponseEntity.ok(paymentInitiatorService.getPaymentStatus(id));
    }
}
