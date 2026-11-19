package com.financialhub.userprofileservice.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class UserProfile {
    private final UUID userId;
    private String displayName;
    private String preferredLocale;
    private Status status;
    private final Instant createdAt;
    private Instant updatedAt;

    public UserProfile(UUID userId, String displayName, String preferredLocale, Status status, Instant createdAt, Instant updatedAt) {
        this.userId = Objects.requireNonNull(userId); this.displayName = text(displayName, "displayName");
        this.preferredLocale = text(preferredLocale, "preferredLocale"); this.status = Objects.requireNonNull(status);
        this.createdAt = Objects.requireNonNull(createdAt); this.updatedAt = Objects.requireNonNull(updatedAt);
    }
    public void update(String displayName, String preferredLocale, Instant now) { this.displayName = text(displayName, "displayName"); this.preferredLocale = text(preferredLocale, "preferredLocale"); this.updatedAt = Objects.requireNonNull(now); }
    public void deactivate(Instant now) { status = Status.DEACTIVATED; updatedAt = Objects.requireNonNull(now); }
    private static String text(String v, String n) { if (v == null || v.isBlank()) throw new IllegalArgumentException(n + " is required"); return v.trim(); }
    public UUID getUserId() { return userId; } public String getDisplayName() { return displayName; } public String getPreferredLocale() { return preferredLocale; }
    public Status getStatus() { return status; } public Instant getCreatedAt() { return createdAt; } public Instant getUpdatedAt() { return updatedAt; }
    public enum Status { ACTIVE, DEACTIVATED }
}
