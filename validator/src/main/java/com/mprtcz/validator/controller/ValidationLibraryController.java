package com.mprtcz.validator.controller;


import com.mprtcz.validator.service.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidationLibraryController implements ValidationController {
    private final ValidationService validationService;

    @Override
    public boolean validate(
        String sourceAccountNumber, String destinationAccountNumber) {
        return validationService.validate(sourceAccountNumber,
            destinationAccountNumber
        );
    }
}
