package com.financialhub.ledgerservice.application.port.out;

import com.financialhub.ledgerservice.domain.IdempotencyRecord;
import java.util.Optional;
import java.util.UUID;

public interface IdempotencyRecordRepository {
    IdempotencyRecord save(IdempotencyRecord record);
    Optional<IdempotencyRecord> findByOwnerIdAndKey(UUID ownerId, String key);
}
