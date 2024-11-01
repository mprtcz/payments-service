package com.mprtcz.service;

import com.mprtcz.config.ValidationRulesProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidationService {
    private final ValidationRulesProperties validationRulesProperties;

    public boolean validate(
        String sourceAccountNumber, String destinationAccountNumber) {
        return !validationRulesProperties.getBlacklistedAccounts()
            .contains(sourceAccountNumber) &&
            !validationRulesProperties.getBlacklistedAccounts()
                .contains(destinationAccountNumber);
    }
}
