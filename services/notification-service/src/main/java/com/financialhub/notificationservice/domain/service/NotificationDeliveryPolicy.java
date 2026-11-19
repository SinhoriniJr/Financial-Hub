package com.financialhub.notificationservice.domain.service;

import com.financialhub.notificationservice.domain.NotificationPreference;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Applies explicit preferences and conservative defaults before a channel delivery is attempted. */
public final class NotificationDeliveryPolicy {
    public boolean isAllowed(NotificationPreference.Channel channel, String notificationType,
                             List<NotificationPreference> preferences) {
        Objects.requireNonNull(channel); Objects.requireNonNull(notificationType); Objects.requireNonNull(preferences);
        Optional<NotificationPreference> preference = preferences.stream()
                .filter(item -> item.channel() == channel && item.notificationType().equals(notificationType))
                .findFirst();
        return preference.map(NotificationPreference::enabled)
                .orElse(channel == NotificationPreference.Channel.IN_APP);
    }
}
