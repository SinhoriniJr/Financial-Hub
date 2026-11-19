package com.financialhub.notificationservice.application.usecase;

import com.financialhub.notificationservice.application.port.out.NotificationRepository;
import com.financialhub.notificationservice.domain.Notification;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class ListNotificationsUseCase {
    private final NotificationRepository notifications;
    public ListNotificationsUseCase(NotificationRepository notifications) { this.notifications = Objects.requireNonNull(notifications); }
    public List<Notification> execute(UUID authenticatedUserId, int limit, int offset) {
        Objects.requireNonNull(authenticatedUserId);
        if (limit < 1 || limit > 100) throw new IllegalArgumentException("limit must be between 1 and 100");
        if (offset < 0) throw new IllegalArgumentException("offset cannot be negative");
        return List.copyOf(notifications.findRecentByOwnerId(authenticatedUserId, limit, offset));
    }
}
