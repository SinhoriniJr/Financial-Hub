package com.financialhub.ledgerservice.interfaces.dto.request;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record CreateTransactionRequest(UUID accountId, String type, BigDecimal amount, String currency,
                                       String description, String categoryId, Instant occurredAt) { }
