package com.financialhub.userprofileservice.interfaces.dto.response;

import java.time.Instant;
import java.util.UUID;

public record ProfileResponse(UUID userId, String displayName, String preferredLocale,
                              String status, Instant createdAt, Instant updatedAt) { }
