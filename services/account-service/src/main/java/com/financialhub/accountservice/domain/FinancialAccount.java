package com.financialhub.accountservice.domain;

import java.time.Instant;
import java.util.Currency;
import java.util.Objects;
import java.util.UUID;

public final class FinancialAccount {
    private final UUID id;
    private final UUID ownerId;
    private String name;
    private final AccountType type;
    private final Currency currency;
    private Status status;
    private final Instant createdAt;
    private Instant updatedAt;

    public FinancialAccount(UUID id, UUID ownerId, String name, AccountType type, Currency currency, Status status, Instant createdAt, Instant updatedAt) {
        this.id = Objects.requireNonNull(id); this.ownerId = Objects.requireNonNull(ownerId); this.name = text(name);
        this.type = Objects.requireNonNull(type); this.currency = Objects.requireNonNull(currency); this.status = Objects.requireNonNull(status);
        this.createdAt = Objects.requireNonNull(createdAt); this.updatedAt = Objects.requireNonNull(updatedAt);
    }
    public static FinancialAccount open(UUID ownerId, String name, AccountType type, Currency currency, Instant now) { return new FinancialAccount(UUID.randomUUID(), ownerId, name, type, currency, Status.ACTIVE, now, now); }
    public void rename(String name, Instant now) { ensureActive(); this.name = text(name); this.updatedAt = Objects.requireNonNull(now); }
    public void close(Instant now) { ensureActive(); status = Status.CLOSED; updatedAt = Objects.requireNonNull(now); }
    private void ensureActive() { if (status != Status.ACTIVE) throw new IllegalStateException("Account is not active"); }
    private static String text(String v) { if (v == null || v.isBlank()) throw new IllegalArgumentException("name is required"); return v.trim(); }
    public UUID getId() { return id; } public UUID getOwnerId() { return ownerId; } public String getName() { return name; }
    public AccountType getType() { return type; } public Currency getCurrency() { return currency; } public Status getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; } public Instant getUpdatedAt() { return updatedAt; }
    public enum AccountType { CHECKING, SAVINGS, CASH, BROKERAGE }
    public enum Status { ACTIVE, CLOSED }
}
