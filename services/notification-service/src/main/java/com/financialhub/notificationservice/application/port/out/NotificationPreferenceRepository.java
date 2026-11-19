package com.financialhub.notificationservice.application.port.out;

import com.financialhub.notificationservice.domain.NotificationPreference;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationPreferenceRepository {
    NotificationPreference save(NotificationPreference preference);
    Optional<NotificationPreference> findByUserIdAndChannelAndType(UUID userId, NotificationPreference.Channel channel, String type);
    List<NotificationPreference> findAllByUserId(UUID userId);
}
