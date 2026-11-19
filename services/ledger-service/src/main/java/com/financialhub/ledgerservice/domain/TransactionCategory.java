package com.financialhub.ledgerservice.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class TransactionCategory {
    private final UUID id;
    private final UUID ownerId;
    private String name;
    private final Kind kind;
    private boolean active;
    private final Instant createdAt;

    public TransactionCategory(UUID id, UUID ownerId, String name, Kind kind, boolean active, Instant createdAt) {
        this.id = Objects.requireNonNull(id); this.ownerId = ownerId; this.name = text(name);
        this.kind = Objects.requireNonNull(kind); this.active = active; this.createdAt = Objects.requireNonNull(createdAt);
    }
    public void rename(String name) { this.name = text(name); }
    public void deactivate() { active = false; }
    private static String text(String value) { if (value == null || value.isBlank()) throw new IllegalArgumentException("name is required"); return value.trim(); }
    public UUID getId() { return id; } public UUID getOwnerId() { return ownerId; } public String getName() { return name; }
    public Kind getKind() { return kind; } public boolean isActive() { return active; } public Instant getCreatedAt() { return createdAt; }
    public enum Kind { INCOME, EXPENSE, BOTH }
}
