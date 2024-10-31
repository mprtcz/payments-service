package com.mprtcz.controllers;

import com.mprtcz.controllers.dto.PaymentRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    @PostMapping("v1/payment/initiate")
    public ResponseEntity<Void> initiatePayment(@RequestBody PaymentRequest paymentRequest) {
        System.out.println("paymentRequest = " + paymentRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/v1/payment/echo")
    public ResponseEntity<String> echo() {
        return ResponseEntity.ok().body("Hello");
    }
}
