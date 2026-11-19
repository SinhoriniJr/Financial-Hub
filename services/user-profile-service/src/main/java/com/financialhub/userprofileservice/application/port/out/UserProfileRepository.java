package com.financialhub.userprofileservice.application.port.out;

import com.financialhub.userprofileservice.domain.UserProfile;
import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository {
    UserProfile save(UserProfile profile);
    Optional<UserProfile> findByUserId(UUID userId);
    boolean existsByUserId(UUID userId);
}
