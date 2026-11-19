package com.financialhub.accountservice.interfaces.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record OpenAccountRequest(@NotBlank @Size(max = 80) String name, @NotBlank String type,
                                 @NotBlank @Pattern(regexp = "^[A-Z]{3}$") String currency) { }
