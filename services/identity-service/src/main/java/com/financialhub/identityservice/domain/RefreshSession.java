package com.financialhub.identityservice.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/** Stores a hash of the refresh token, never the raw bearer token. */
public final class RefreshSession {
    private final UUID id;
    private final UUID userId;
    private final String tokenHash;
    private final Instant createdAt;
    private final Instant expiresAt;
    private Instant revokedAt;

    public RefreshSession(UUID id, UUID userId, String tokenHash, Instant createdAt, Instant expiresAt, Instant revokedAt) {
        this.id = Objects.requireNonNull(id); this.userId = Objects.requireNonNull(userId);
        if (tokenHash == null || tokenHash.isBlank()) throw new IllegalArgumentException("tokenHash is required");
        this.tokenHash = tokenHash; this.createdAt = Objects.requireNonNull(createdAt); this.expiresAt = Objects.requireNonNull(expiresAt); this.revokedAt = revokedAt;
        if (!expiresAt.isAfter(createdAt)) throw new IllegalArgumentException("expiresAt must be after createdAt");
    }
    public boolean isUsableAt(Instant now) { return revokedAt == null && Objects.requireNonNull(now).isBefore(expiresAt); }
    public void revoke(Instant now) { if (revokedAt == null) revokedAt = Objects.requireNonNull(now); }
    public UUID getId() { return id; } public UUID getUserId() { return userId; } public String getTokenHash() { return tokenHash; }
    public Instant getCreatedAt() { return createdAt; } public Instant getExpiresAt() { return expiresAt; } public Instant getRevokedAt() { return revokedAt; }
}
