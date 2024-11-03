package com.mprtcz.transaction.repository.data;

import java.math.BigDecimal;

public record Account(long id, String accountNumber, BigDecimal balance) {
}