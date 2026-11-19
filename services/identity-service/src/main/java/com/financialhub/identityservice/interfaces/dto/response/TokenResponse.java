package com.financialhub.identityservice.interfaces.dto.response;

import java.time.Instant;
import java.util.UUID;

public record TokenResponse(String accessToken, String refreshToken, String tokenType,
                            Instant accessTokenExpiresAt, UUID userId, String role) { }
