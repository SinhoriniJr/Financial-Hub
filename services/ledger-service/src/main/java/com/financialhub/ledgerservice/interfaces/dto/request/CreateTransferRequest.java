package com.financialhub.ledgerservice.interfaces.dto.request;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record CreateTransferRequest(UUID sourceAccountId, UUID destinationAccountId, BigDecimal amount,
                                   String currency, String description, Instant occurredAt) { }
