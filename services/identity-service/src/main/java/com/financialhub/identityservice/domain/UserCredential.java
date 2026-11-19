package com.financialhub.identityservice.domain;

import java.time.Instant;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

/** Identity aggregate. Passwords are stored only as one-way hashes. */
public final class UserCredential {
    private final UUID id;
    private final String email;
    private String passwordHash;
    private Role role;
    private Status status;
    private final Instant createdAt;
    private Instant updatedAt;

    public UserCredential(UUID id, String email, String passwordHash, Role role,
                          Status status, Instant createdAt, Instant updatedAt) {
        this.id = Objects.requireNonNull(id);
        this.email = normalizeEmail(email);
        this.passwordHash = requireText(passwordHash, "passwordHash");
        this.role = Objects.requireNonNull(role);
        this.status = Objects.requireNonNull(status);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.updatedAt = Objects.requireNonNull(updatedAt);
    }

    public static UserCredential register(String email, String passwordHash, Instant now) {
        return new UserCredential(UUID.randomUUID(), email, passwordHash, Role.USER, Status.PENDING_VERIFICATION, now, now);
    }

    public void activate(Instant now) { if (status != Status.PENDING_VERIFICATION) throw new IllegalStateException("User is not pending verification"); status = Status.ACTIVE; updatedAt = Objects.requireNonNull(now); }
    public void disable(Instant now) { status = Status.DISABLED; updatedAt = Objects.requireNonNull(now); }
    public void changePasswordHash(String hash, Instant now) { passwordHash = requireText(hash, "passwordHash"); updatedAt = Objects.requireNonNull(now); }
    public void changeRole(Role role, Instant now) { this.role = Objects.requireNonNull(role); updatedAt = Objects.requireNonNull(now); }
    private static String normalizeEmail(String value) { String email = requireText(value, "email").toLowerCase(Locale.ROOT); if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) throw new IllegalArgumentException("Invalid email"); return email; }
    private static String requireText(String value, String name) { if (value == null || value.isBlank()) throw new IllegalArgumentException(name + " is required"); return value.trim(); }
    public UUID getId() { return id; } public String getEmail() { return email; } public String getPasswordHash() { return passwordHash; }
    public Role getRole() { return role; } public Status getStatus() { return status; } public Instant getCreatedAt() { return createdAt; } public Instant getUpdatedAt() { return updatedAt; }
    public enum Role { USER, ADMIN }
    public enum Status { PENDING_VERIFICATION, ACTIVE, DISABLED }
}
