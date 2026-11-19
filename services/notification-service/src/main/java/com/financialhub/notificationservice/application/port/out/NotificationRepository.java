package com.financialhub.notificationservice.application.port.out;

import com.financialhub.notificationservice.domain.Notification;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository {
    Notification save(Notification notification);
    Optional<Notification> findByIdAndOwnerId(UUID notificationId, UUID ownerId);
    List<Notification> findRecentByOwnerId(UUID ownerId, int limit, int offset);
    boolean existsByEventIdAndOwnerId(String eventId, UUID ownerId);
}
