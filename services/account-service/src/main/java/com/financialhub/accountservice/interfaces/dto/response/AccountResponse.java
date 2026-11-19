package com.financialhub.accountservice.interfaces.dto.response;

import java.time.Instant;
import java.util.UUID;

public record AccountResponse(UUID id, UUID ownerId, String name, String type, String currency,
                              String status, Instant createdAt, Instant updatedAt) { }
