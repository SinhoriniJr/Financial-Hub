package com.financialhub.ledgerservice.interfaces.dto.request;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record CreateTransactionRequest(@NotNull UUID accountId, @NotBlank String type, @NotNull @Positive BigDecimal amount,
                                       @NotBlank @Pattern(regexp = "^[A-Z]{3}$") String currency,
                                       @NotBlank String description, String categoryId, Instant occurredAt) { }
