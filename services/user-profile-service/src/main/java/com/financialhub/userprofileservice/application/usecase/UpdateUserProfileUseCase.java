package com.financialhub.userprofileservice.application.usecase;

import com.financialhub.userprofileservice.application.port.out.UserProfileRepository;
import com.financialhub.userprofileservice.domain.UserProfile;
import java.time.Clock;
import java.util.Objects;
import java.util.UUID;

public final class UpdateUserProfileUseCase {
    private final UserProfileRepository profiles;
    private final Clock clock;

    public UpdateUserProfileUseCase(UserProfileRepository profiles, Clock clock) { this.profiles = Objects.requireNonNull(profiles); this.clock = Objects.requireNonNull(clock); }
    public UserProfile execute(UUID authenticatedUserId, String displayName, String preferredLocale) {
        UserProfile profile = profiles.findByUserId(Objects.requireNonNull(authenticatedUserId))
                .orElseThrow(() -> new IllegalStateException("User profile was not found"));
        profile.update(displayName, preferredLocale, clock.instant());
        return profiles.save(profile);
    }
}
