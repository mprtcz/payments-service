package com.mprtcz.transaction.repository;

import com.github.tomakehurst.wiremock.admin.NotFoundException;
import com.mprtcz.transaction.repository.data.Account;
import lombok.RequiredArgsConstructor;
import org.assertj.core.util.VisibleForTesting;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PaymentTransactionRepository {
    private static final RowMapper<Account> ACCOUNT_ROW_MAPPER =
            (rs, rowNum) -> new Account(
            rs.getLong("id"),
            rs.getString("account_number"),
            rs.getBigDecimal("balance"));
    private static final String ACCOUNT_SELECT = """
            SELECT * FROM accounts WHERE account_number = ?
            """;
    public static final String ACCOUNT_BALANCE_UPDATE =
            """
              UPDATE accounts SET balance = balance + ? WHERE account_number = ?
            """;
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void transfer(String fromAccount, String toAccount, BigDecimal amount) {
        var fromAccountOpt = findByAccountNumber(fromAccount);
        System.out.println("fromAccountOpt = " + fromAccountOpt);
        if (fromAccountOpt.isEmpty()) {
            throw new NotFoundException("Account not found");
        }
        if (fromAccountOpt.get().balance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        int debitResult = updateBalance(fromAccount, amount.negate());
        if (debitResult == 0) {
            throw new IllegalStateException("Failed to debit the amount");
        }

        int creditResult = updateBalance(toAccount, amount);
        if (creditResult == 0) {
            throw new IllegalStateException("Failed to credit the amount");
        }
    }

    @VisibleForTesting
    public Optional<Account> findByAccountNumber(String accountNumber) {
        return jdbcTemplate.query(ACCOUNT_SELECT, ACCOUNT_ROW_MAPPER, accountNumber)
                .stream()
                .findFirst();
    }

    private int updateBalance(String accountNumber, BigDecimal amount) {
        return jdbcTemplate.update(ACCOUNT_BALANCE_UPDATE, amount, accountNumber);
    }
}
