package com.mprtcz.validators;

import java.util.concurrent.CompletableFuture;

public interface TransactionValidator {
    CompletableFuture<Boolean> isValid(String sourceAccountNumber, String destinationAccountNumber);
}
