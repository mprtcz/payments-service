package com.mprtcz.transaction.dto;

import java.math.BigDecimal;

public record TransactionRequest(String paymentRequesterAccountNumber,
                                 String paymentDestinationAccountNumber,
                                 BigDecimal amount, String id)
        implements Identifiable {

    @Override
    public String getId() {
        return this.id;
    }
}
