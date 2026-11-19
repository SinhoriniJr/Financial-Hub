package com.financialhub.userprofileservice.interfaces.controller;

import com.financialhub.userprofileservice.application.usecase.UpdateUserProfileUseCase;
import com.financialhub.userprofileservice.interfaces.dto.request.UpdateProfileRequest;
import com.financialhub.userprofileservice.interfaces.dto.response.ProfileResponse;
import java.security.Principal;
import java.util.UUID;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/profile")
public class UserProfileController {
    private final UpdateUserProfileUseCase updateProfile;
    public UserProfileController(UpdateUserProfileUseCase updateProfile) { this.updateProfile = updateProfile; }

    @PutMapping
    public ProfileResponse update(@Valid @RequestBody UpdateProfileRequest request, Principal principal) {
        var profile = updateProfile.execute(userId(principal), request.displayName(), request.preferredLocale());
        return new ProfileResponse(profile.getUserId(), profile.getDisplayName(), profile.getPreferredLocale(),
                profile.getStatus().name(), profile.getCreatedAt(), profile.getUpdatedAt());
    }
    private static UUID userId(Principal principal) { return UUID.fromString(principal.getName()); }
}
