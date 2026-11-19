package com.financialhub.notificationservice.domain;

import java.util.Objects;
import java.util.UUID;

public record NotificationPreference(UUID userId, Channel channel, String notificationType, boolean enabled) {
    public NotificationPreference { Objects.requireNonNull(userId); Objects.requireNonNull(channel); if (notificationType == null || notificationType.isBlank()) throw new IllegalArgumentException("notificationType is required"); notificationType = notificationType.trim(); }
    public enum Channel { IN_APP, EMAIL }
}
