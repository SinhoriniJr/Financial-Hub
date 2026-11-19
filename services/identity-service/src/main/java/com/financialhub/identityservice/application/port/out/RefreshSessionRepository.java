package com.financialhub.identityservice.application.port.out;

import com.financialhub.identityservice.domain.RefreshSession;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshSessionRepository {
    RefreshSession save(RefreshSession session);
    Optional<RefreshSession> findByTokenHash(String tokenHash);
    List<RefreshSession> findUsableByUserId(UUID userId, Instant at);
}
