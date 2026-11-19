package com.financialhub.identityservice.interfaces.dto.response;

import java.time.Instant;
import java.util.UUID;

public record RegistrationResponse(UUID userId, String email, String status, Instant createdAt) { }
