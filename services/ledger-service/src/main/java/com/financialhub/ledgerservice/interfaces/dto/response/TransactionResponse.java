package com.financialhub.ledgerservice.interfaces.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record TransactionResponse(UUID id, String type, String description, String categoryId,
                                  Instant occurredAt, List<EntryResponse> entries, UUID reversesTransactionId) {
    public TransactionResponse { entries = List.copyOf(entries); }
    public record EntryResponse(UUID accountId, String direction, BigDecimal amount, String currency) { }
}
