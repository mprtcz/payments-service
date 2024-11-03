package com.mprtcz.validator.service;

import com.mprtcz.validator.validators.TransactionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ValidationService {
    private final List<TransactionValidator> validators;

    public boolean validate(String sourceAccountNumber,
            String destinationAccountNumber) {
        return validators.stream()
                .map(validator -> validator
                        .isValid(sourceAccountNumber, destinationAccountNumber))
                .allMatch(CompletableFuture::join);
    }
}
