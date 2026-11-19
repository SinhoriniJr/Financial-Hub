package com.financialhub.investmentservice.interfaces.dto.request;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record RecordTradeRequest(@NotNull UUID investmentAccountId, @NotBlank String assetSymbol, @NotBlank String type,
                                 @NotNull @Positive BigDecimal quantity, @NotNull @Positive BigDecimal unitPrice,
                                 @NotNull BigDecimal fees, @NotBlank @Pattern(regexp = "^[A-Z]{3}$") String currency,
                                 Instant executedAt) { }
