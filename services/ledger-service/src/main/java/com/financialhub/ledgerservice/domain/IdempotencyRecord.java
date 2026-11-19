package com.financialhub.ledgerservice.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/** Scoped idempotency key and the operation result needed to answer safe retries. */
public record IdempotencyRecord(UUID id, UUID ownerId, String key, String requestFingerprint,
                                UUID transactionId, Instant createdAt, Instant expiresAt) {
    public IdempotencyRecord {
        Objects.requireNonNull(id); Objects.requireNonNull(ownerId); Objects.requireNonNull(transactionId);
        if (key == null || key.isBlank()) throw new IllegalArgumentException("key is required");
        if (requestFingerprint == null || requestFingerprint.isBlank()) throw new IllegalArgumentException("requestFingerprint is required");
        Objects.requireNonNull(createdAt); Objects.requireNonNull(expiresAt);
        if (!expiresAt.isAfter(createdAt)) throw new IllegalArgumentException("expiresAt must be after createdAt");
        key = key.trim();
    }
}
