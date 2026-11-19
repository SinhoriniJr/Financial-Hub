package com.financialhub.ledgerservice.domain.service;

import com.financialhub.ledgerservice.domain.FinancialTransaction;
import com.financialhub.ledgerservice.domain.FinancialTransaction.Direction;
import com.financialhub.ledgerservice.domain.FinancialTransaction.LedgerEntry;
import com.financialhub.ledgerservice.domain.FinancialTransaction.TransactionType;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/** Creates balanced immutable ledger transactions using the normal-balance convention for asset accounts. */
public final class LedgerPostingService {
    public FinancialTransaction recordIncome(UUID transactionId, UUID ownerId, UUID cashAccountId,
                                             UUID incomeCounterAccountId, BigDecimal amount, Currency currency,
                                             String description, String categoryId, Instant occurredAt) {
        return external(transactionId, ownerId, cashAccountId, incomeCounterAccountId, amount, currency,
                description, categoryId, occurredAt, TransactionType.INCOME, Direction.DEBIT, Direction.CREDIT);
    }

    public FinancialTransaction recordExpense(UUID transactionId, UUID ownerId, UUID cashAccountId,
                                              UUID expenseCounterAccountId, BigDecimal amount, Currency currency,
                                              String description, String categoryId, Instant occurredAt) {
        return external(transactionId, ownerId, cashAccountId, expenseCounterAccountId, amount, currency,
                description, categoryId, occurredAt, TransactionType.EXPENSE, Direction.CREDIT, Direction.DEBIT);
    }

    public FinancialTransaction recordTransfer(UUID transactionId, UUID ownerId, UUID sourceAccountId,
                                               UUID destinationAccountId, BigDecimal amount, Currency currency,
                                               String description, Instant occurredAt) {
        if (Objects.equals(sourceAccountId, destinationAccountId)) throw new IllegalArgumentException("Transfer accounts must be different");
        requireAmount(amount);
        return new FinancialTransaction(transactionId, ownerId, TransactionType.TRANSFER,
                List.of(new LedgerEntry(sourceAccountId, Direction.CREDIT, amount, currency),
                        new LedgerEntry(destinationAccountId, Direction.DEBIT, amount, currency)),
                description, null, occurredAt, null);
    }

    public FinancialTransaction reverse(UUID reversalId, UUID ownerId, FinancialTransaction original,
                                        String description, Instant occurredAt) {
        Objects.requireNonNull(original);
        if (!original.getOwnerId().equals(ownerId)) throw new IllegalArgumentException("Transaction owner mismatch");
        List<LedgerEntry> reversingEntries = original.getEntries().stream()
                .map(entry -> new LedgerEntry(entry.accountId(), opposite(entry.direction()), entry.amount(), entry.currency()))
                .toList();
        return new FinancialTransaction(reversalId, ownerId, TransactionType.REVERSAL, reversingEntries,
                description, original.getCategoryId(), occurredAt, original.getId());
    }

    private FinancialTransaction external(UUID id, UUID ownerId, UUID cashAccountId, UUID counterAccountId,
                                           BigDecimal amount, Currency currency, String description, String categoryId,
                                           Instant occurredAt, TransactionType type, Direction cashDirection, Direction counterDirection) {
        if (Objects.equals(cashAccountId, counterAccountId)) throw new IllegalArgumentException("Ledger accounts must be different");
        requireAmount(amount);
        return new FinancialTransaction(id, ownerId, type,
                List.of(new LedgerEntry(cashAccountId, cashDirection, amount, currency),
                        new LedgerEntry(counterAccountId, counterDirection, amount, currency)),
                description, categoryId, occurredAt, null);
    }

    private static Direction opposite(Direction direction) { return direction == Direction.DEBIT ? Direction.CREDIT : Direction.DEBIT; }
    private static void requireAmount(BigDecimal amount) { if (amount == null || amount.signum() <= 0) throw new IllegalArgumentException("amount must be positive"); }
}
