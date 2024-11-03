package com.mprtcz.validator.validators;

import com.mprtcz.validator.config.ValidationRulesProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Component
@Slf4j
public class BlacklistValidator implements TransactionValidator {
    private final ValidationRulesProperties validationRulesProperties;

    @Override
    @Async
    public CompletableFuture<Boolean> isValid(String sourceAccountNumber,
            String destinationAccountNumber) {
        var result = !validationRulesProperties.getBlacklistedAccounts()
                .contains(sourceAccountNumber) && !validationRulesProperties.getBlacklistedAccounts()
                .contains(destinationAccountNumber);
        if (!result) {
            log.warn(
                    "Account number (source) {} or (destination) {} is " +
                            "blacklisted",
                    sourceAccountNumber,
                    destinationAccountNumber);
        }
        return CompletableFuture.completedFuture(result);
    }
}
