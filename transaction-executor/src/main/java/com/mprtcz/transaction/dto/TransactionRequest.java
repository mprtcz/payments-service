package com.mprtcz.transaction.dto;


import java.math.BigDecimal;

public record TransactionRequest(String paymentRequesterAccountNumber,
                                 String paymentDestinationAccountNumber,
                                 BigDecimal amount) {
}
