package com.financialhub.ledgerservice.application.port.out;

import com.financialhub.ledgerservice.domain.FinancialTransaction;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FinancialTransactionRepository {
    /** Must persist the transaction and all ledger entries atomically. */
    FinancialTransaction save(FinancialTransaction transaction);
    Optional<FinancialTransaction> findByIdAndOwnerId(UUID transactionId, UUID ownerId);
    List<FinancialTransaction> findByOwnerIdAndOccurredAtBetween(UUID ownerId, Instant from, Instant to, int limit, int offset);
    boolean existsReversalFor(UUID transactionId, UUID ownerId);
}
