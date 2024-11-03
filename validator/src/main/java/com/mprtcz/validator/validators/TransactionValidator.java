package com.mprtcz.validator.validators;

import java.util.concurrent.CompletableFuture;

public interface TransactionValidator {
    CompletableFuture<Boolean> isValid(String sourceAccountNumber, String destinationAccountNumber);
}
