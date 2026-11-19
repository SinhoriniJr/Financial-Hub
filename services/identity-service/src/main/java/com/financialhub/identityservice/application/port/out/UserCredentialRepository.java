package com.financialhub.identityservice.application.port.out;

import com.financialhub.identityservice.domain.UserCredential;
import java.util.Optional;
import java.util.UUID;

/** Persistence port owned by the identity application. */
public interface UserCredentialRepository {
    UserCredential save(UserCredential credential);
    Optional<UserCredential> findById(UUID userId);
    Optional<UserCredential> findByEmail(String normalizedEmail);
    boolean existsByEmail(String normalizedEmail);
}
