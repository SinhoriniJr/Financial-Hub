package com.financialhub.userprofileservice.interfaces.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(@NotBlank @Size(max = 120) String displayName,
                                   @NotBlank @Pattern(regexp = "^[a-z]{2}(-[A-Z]{2})?$") String preferredLocale) { }
