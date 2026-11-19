package com.financialhub.notificationservice.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class Notification {
    private final UUID id;
    private final UUID ownerId;
    private final String eventId;
    private final String type;
    private final String title;
    private final String message;
    private final Instant createdAt;
    private Status status;
    private Instant deliveredAt;

    public Notification(UUID id, UUID ownerId, String eventId, String type, String title, String message, Instant createdAt, Status status, Instant deliveredAt) {
        this.id = Objects.requireNonNull(id); this.ownerId = Objects.requireNonNull(ownerId); this.eventId = text(eventId, "eventId");
        this.type = text(type, "type"); this.title = text(title, "title"); this.message = text(message, "message"); this.createdAt = Objects.requireNonNull(createdAt);
        this.status = Objects.requireNonNull(status); this.deliveredAt = deliveredAt;
    }
    public void markDelivered(Instant at) { if (status == Status.DELIVERED) return; if (status == Status.DISMISSED) throw new IllegalStateException("Dismissed notification cannot be delivered"); deliveredAt = Objects.requireNonNull(at); status = Status.DELIVERED; }
    public void dismiss() { status = Status.DISMISSED; }
    private static String text(String v, String n) { if (v == null || v.isBlank()) throw new IllegalArgumentException(n + " is required"); return v.trim(); }
    public UUID getId() { return id; } public UUID getOwnerId() { return ownerId; } public String getEventId() { return eventId; } public String getType() { return type; }
    public String getTitle() { return title; } public String getMessage() { return message; } public Instant getCreatedAt() { return createdAt; } public Status getStatus() { return status; } public Instant getDeliveredAt() { return deliveredAt; }
    public enum Status { PENDING, DELIVERED, FAILED, DISMISSED }
}
