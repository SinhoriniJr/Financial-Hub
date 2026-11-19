package com.financialhub.ledgerservice.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/** Immutable, balanced ledger operation. Account balances are derived from posted entries. */
public final class FinancialTransaction {
    private final UUID id;
    private final UUID ownerId;
    private final TransactionType type;
    private final List<LedgerEntry> entries;
    private final String description;
    private final String categoryId;
    private final Instant occurredAt;
    private final UUID reversesTransactionId;

    public FinancialTransaction(UUID id, UUID ownerId, TransactionType type, List<LedgerEntry> entries,
                                String description, String categoryId, Instant occurredAt, UUID reversesTransactionId) {
        this.id = Objects.requireNonNull(id); this.ownerId = Objects.requireNonNull(ownerId); this.type = Objects.requireNonNull(type);
        this.entries = List.copyOf(Objects.requireNonNull(entries)); this.description = text(description, "description");
        this.categoryId = categoryId == null || categoryId.isBlank() ? null : categoryId.trim(); this.occurredAt = Objects.requireNonNull(occurredAt);
        this.reversesTransactionId = reversesTransactionId; validateBalance(this.entries);
        if (type == TransactionType.REVERSAL && reversesTransactionId == null) throw new IllegalArgumentException("Reversal must reference original transaction");
    }
    private static void validateBalance(List<LedgerEntry> entries) {
        if (entries.size() < 2) throw new IllegalArgumentException("A ledger transaction requires at least two entries");
        Currency currency = entries.getFirst().currency(); BigDecimal total = BigDecimal.ZERO;
        for (LedgerEntry e : entries) { if (!currency.equals(e.currency())) throw new IllegalArgumentException("Entries must use the same currency"); total = total.add(e.amount().multiply(e.direction().sign)); }
        if (total.compareTo(BigDecimal.ZERO) != 0) throw new IllegalArgumentException("Ledger entries must balance to zero");
    }
    private static String text(String v, String n) { if (v == null || v.isBlank()) throw new IllegalArgumentException(n + " is required"); return v.trim(); }
    public UUID getId() { return id; } public UUID getOwnerId() { return ownerId; } public TransactionType getType() { return type; }
    public List<LedgerEntry> getEntries() { return entries; } public String getDescription() { return description; } public String getCategoryId() { return categoryId; }
    public Instant getOccurredAt() { return occurredAt; } public UUID getReversesTransactionId() { return reversesTransactionId; }
    public enum TransactionType { INCOME, EXPENSE, TRANSFER, REVERSAL }
    public enum Direction { DEBIT(1), CREDIT(-1); private final int sign; Direction(int sign) { this.sign = sign; } }
    public record LedgerEntry(UUID accountId, Direction direction, BigDecimal amount, Currency currency) {
        public LedgerEntry { Objects.requireNonNull(accountId); Objects.requireNonNull(direction); Objects.requireNonNull(currency); if (amount == null || amount.signum() <= 0) throw new IllegalArgumentException("amount must be positive"); }
    }
}
