package com.mprtcz.initiator.controllers;

import com.mprtcz.initiator.controllers.dto.PaymentRequest;
import com.mprtcz.initiator.controllers.dto.PaymentResponse;
import com.mprtcz.initiator.controllers.dto.PaymentStatusResponse;
import com.mprtcz.initiator.services.PaymentInitiatorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/payment")
public class PaymentController {
    private final PaymentInitiatorService paymentInitiatorService;

    @PostMapping(value = "/initiate",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PaymentResponse> initiatePayment(
        @Valid @RequestBody PaymentRequest paymentRequest) {
        log.info("paymentRequest = {}", paymentRequest);
        var id = paymentInitiatorService.processPayment(paymentRequest);
        return ResponseEntity.accepted().body(new PaymentResponse(id));
    }

    @GetMapping(value = "/status/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PaymentStatusResponse> getPaymentStatus(
        @PathVariable("id") String id) {
        return ResponseEntity.ok()
            .body(PaymentStatusResponse.create(
                paymentInitiatorService.getPaymentStatus(id)));
    }
}
