package com.mprtcz.controllers;

import com.mprtcz.controllers.dto.PaymentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class PaymentController {

    @PostMapping("/v1/payment/initiate")
    public ResponseEntity<Void> initiatePayment(@RequestBody PaymentRequest paymentRequest) {
        log.info("paymentRequest = {}", paymentRequest);
        return ResponseEntity.ok().build();
    }
}
