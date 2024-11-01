package com.mprtcz.controller;


import com.mprtcz.service.ValidationService;
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
