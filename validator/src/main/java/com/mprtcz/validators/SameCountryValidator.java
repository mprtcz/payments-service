package com.mprtcz.validators;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class SameCountryValidator implements TransactionValidator {

    @Override
    @Async
    public CompletableFuture<Boolean> isValid(String sourceAccountNumber,
            String destinationAccountNumber) {
        var result = sourceAccountNumber.substring(0, 2)
                .equals(destinationAccountNumber.substring(0, 2));
        if (!result) {
            log.warn(
                    "Account number (source) {} and (destination) {} are not in the same country",
                    sourceAccountNumber,
                    destinationAccountNumber);
        }
        return CompletableFuture.completedFuture(result);
    }
}