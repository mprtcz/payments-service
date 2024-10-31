package com.mprtcz.controllers.dto;

import java.math.BigDecimal;

public class PaymentRequest {
    Long paymentRequesterUserId;
    Long paymentDestinationUserId;
    BigDecimal amount;
}
