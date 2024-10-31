package com.mprtcz.payments;

import com.mprtcz.services.PaymentInitiatorService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class MyController {
    PaymentInitiatorService paymentInitiatorService;

    @GetMapping
    public ResponseEntity<String> get() {
        paymentInitiatorService.processPayment(null);
        return ResponseEntity.ok("Hello");
    }
}
