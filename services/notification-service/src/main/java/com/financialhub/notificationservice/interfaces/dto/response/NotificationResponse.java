package com.financialhub.notificationservice.interfaces.dto.response;

import java.time.Instant;
import java.util.UUID;

public record NotificationResponse(UUID id, String type, String title, String message,
                                  Instant createdAt, String status, Instant deliveredAt) { }
