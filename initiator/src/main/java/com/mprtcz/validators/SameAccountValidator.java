package com.mprtcz.validators;

import com.mprtcz.controllers.dto.PaymentRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SameAccountValidator  implements ConstraintValidator<NotSameAccount, PaymentRequest> {

    @Override
    public boolean isValid(PaymentRequest dto, ConstraintValidatorContext context) {
        return !dto.getPaymentRequesterAccountNumber()
                .equals(dto.getPaymentDestinationAccountNumber());
    }
}
