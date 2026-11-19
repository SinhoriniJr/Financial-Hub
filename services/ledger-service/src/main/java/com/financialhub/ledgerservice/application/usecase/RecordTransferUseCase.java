package com.financialhub.ledgerservice.application.usecase;

import com.financialhub.ledgerservice.application.port.out.FinancialTransactionRepository;
import com.financialhub.ledgerservice.application.port.out.IdempotencyRecordRepository;
import com.financialhub.ledgerservice.application.port.out.LedgerAccountAccess;
import com.financialhub.ledgerservice.application.port.out.LedgerOperationStore;
import com.financialhub.ledgerservice.domain.FinancialTransaction;
import com.financialhub.ledgerservice.domain.IdempotencyRecord;
import com.financialhub.ledgerservice.domain.service.LedgerPostingService;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Duration;
import java.util.Currency;
import java.util.Objects;
import java.util.UUID;

public final class RecordTransferUseCase {
    private static final Duration KEY_RETENTION = Duration.ofHours(24);
    private final LedgerAccountAccess accountAccess;
    private final IdempotencyRecordRepository idempotencyRecords;
    private final FinancialTransactionRepository transactions;
    private final LedgerOperationStore operationStore;
    private final LedgerPostingService postingService;
    private final Clock clock;

    public RecordTransferUseCase(LedgerAccountAccess accountAccess, IdempotencyRecordRepository idempotencyRecords,
            FinancialTransactionRepository transactions, LedgerOperationStore operationStore,
            LedgerPostingService postingService, Clock clock) {
        this.accountAccess = Objects.requireNonNull(accountAccess); this.idempotencyRecords = Objects.requireNonNull(idempotencyRecords);
        this.transactions = Objects.requireNonNull(transactions); this.operationStore = Objects.requireNonNull(operationStore);
        this.postingService = Objects.requireNonNull(postingService); this.clock = Objects.requireNonNull(clock);
    }

    public FinancialTransaction execute(UUID ownerId, UUID sourceId, UUID destinationId, BigDecimal amount,
                                        Currency requestedCurrency, String description, java.time.Instant occurredAt, String idempotencyKey) {
        Objects.requireNonNull(ownerId); requireText(idempotencyKey, "idempotencyKey");
        String fingerprint = String.join("|", sourceId.toString(), destinationId.toString(), amount.toPlainString(),
                Objects.toString(requestedCurrency, ""), Objects.toString(description, ""), String.valueOf(occurredAt));
        var previous = idempotencyRecords.findByOwnerIdAndKey(ownerId, idempotencyKey.trim());
        if (previous.isPresent()) {
            IdempotencyRecord record = previous.get();
            if (!record.requestFingerprint().equals(fingerprint)) throw new IllegalStateException("Idempotency key was already used for a different request");
            return transactions.findByIdAndOwnerId(record.transactionId(), ownerId)
                    .orElseThrow(() -> new IllegalStateException("Idempotent transaction result is unavailable"));
        }
        LedgerAccountAccess.AccountSnapshot source = accountAccess.findActiveOwnedAccount(sourceId, ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Source account was not found or is not accessible"));
        LedgerAccountAccess.AccountSnapshot destination = accountAccess.findActiveOwnedAccount(destinationId, ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Destination account was not found or is not accessible"));
        Currency currency = source.currency();
        if (!currency.equals(destination.currency())) throw new IllegalArgumentException("Transfer accounts must use the same currency");
        if (!currency.equals(Objects.requireNonNull(requestedCurrency))) throw new IllegalArgumentException("Currency must match both accounts");
        var now = clock.instant();
        var transaction = postingService.recordTransfer(UUID.randomUUID(), ownerId, sourceId, destinationId, amount,
                currency, description, occurredAt == null ? now : occurredAt);
        var record = new IdempotencyRecord(UUID.randomUUID(), ownerId, idempotencyKey.trim(), fingerprint,
                transaction.getId(), now, now.plus(KEY_RETENTION));
        return operationStore.saveAtomically(transaction, record);
    }
    private static void requireText(String value, String field) { if (value == null || value.isBlank()) throw new IllegalArgumentException(field + " is required"); }
}
