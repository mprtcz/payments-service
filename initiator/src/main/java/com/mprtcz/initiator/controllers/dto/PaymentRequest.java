package com.mprtcz.initiator.controllers.dto;

import com.mprtcz.initiator.validators.Numeric;
import com.mprtcz.initiator.validators.NotSameAccount;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@NotSameAccount
public class PaymentRequest {
    @NotNull(message = "Payment requester account number missing")
    @Size(min = 18, max = 18, message = "Account number invalid")
    @Numeric
    String paymentRequesterAccountNumber;

    @NotNull(message = "Payment destination account number missing")
    @Size(min = 18, max = 18, message = "Account number invalid")
    @Numeric
    String paymentDestinationAccountNumber;

    @NotNull(message = "Payment amount not present")
    @DecimalMin(value = "0.0", inclusive = false, message = "Invalid amount to transfer")
    BigDecimal amount;
}
