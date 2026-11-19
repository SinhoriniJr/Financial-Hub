package com.financialhub.notificationservice.interfaces.dto.request;

public record UpdateNotificationPreferenceRequest(String channel, String notificationType, boolean enabled) { }
