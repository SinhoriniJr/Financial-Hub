package com.financialhub.ledgerservice.application.usecase;

import com.financialhub.ledgerservice.application.port.out.FinancialTransactionRepository;
import com.financialhub.ledgerservice.application.port.out.IdempotencyRecordRepository;
import com.financialhub.ledgerservice.application.port.out.LedgerAccountAccess;
import com.financialhub.ledgerservice.application.port.out.LedgerCounterAccountAccess;
import com.financialhub.ledgerservice.application.port.out.LedgerOperationStore;
import com.financialhub.ledgerservice.application.port.out.TransactionCategoryRepository;
import com.financialhub.ledgerservice.domain.FinancialTransaction;
import com.financialhub.ledgerservice.domain.IdempotencyRecord;
import com.financialhub.ledgerservice.domain.TransactionCategory;
import com.financialhub.ledgerservice.domain.service.LedgerPostingService;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Currency;
import java.util.Objects;
import java.util.UUID;

public final class RecordAccountTransactionUseCase {
    private static final Duration KEY_RETENTION = Duration.ofHours(24);
    private final LedgerAccountAccess accounts;
    private final LedgerCounterAccountAccess counterAccounts;
    private final TransactionCategoryRepository categories;
    private final IdempotencyRecordRepository idempotencyRecords;
    private final FinancialTransactionRepository transactions;
    private final LedgerOperationStore operationStore;
    private final LedgerPostingService postingService;
    private final Clock clock;

    public RecordAccountTransactionUseCase(LedgerAccountAccess accounts, LedgerCounterAccountAccess counterAccounts,
            TransactionCategoryRepository categories, IdempotencyRecordRepository idempotencyRecords,
            FinancialTransactionRepository transactions, LedgerOperationStore operationStore,
            LedgerPostingService postingService, Clock clock) {
        this.accounts = Objects.requireNonNull(accounts); this.counterAccounts = Objects.requireNonNull(counterAccounts);
        this.categories = Objects.requireNonNull(categories); this.idempotencyRecords = Objects.requireNonNull(idempotencyRecords);
        this.transactions = Objects.requireNonNull(transactions); this.operationStore = Objects.requireNonNull(operationStore);
        this.postingService = Objects.requireNonNull(postingService); this.clock = Objects.requireNonNull(clock);
    }

    public FinancialTransaction execute(UUID ownerId, UUID accountId, FinancialTransaction.TransactionType type,
            BigDecimal amount, String description, String categoryId, Instant occurredAt, String idempotencyKey) {
        Objects.requireNonNull(ownerId); Objects.requireNonNull(type);
        if (type != FinancialTransaction.TransactionType.INCOME && type != FinancialTransaction.TransactionType.EXPENSE)
            throw new IllegalArgumentException("Only income and expense are supported by this operation");
        if (amount == null || amount.signum() <= 0) throw new IllegalArgumentException("amount must be positive");
        if (idempotencyKey == null || idempotencyKey.isBlank()) throw new IllegalArgumentException("idempotencyKey is required");
        String fingerprint = String.join("|", accountId.toString(), type.name(), amount.toPlainString(),
                Objects.toString(description, ""), Objects.toString(categoryId, ""), Objects.toString(occurredAt, ""));
        var prior = idempotencyRecords.findByOwnerIdAndKey(ownerId, idempotencyKey.trim());
        if (prior.isPresent()) {
            var record = prior.get();
            if (!record.requestFingerprint().equals(fingerprint)) throw new IllegalStateException("Idempotency key was already used for a different request");
            return transactions.findByIdAndOwnerId(record.transactionId(), ownerId)
                    .orElseThrow(() -> new IllegalStateException("Idempotent transaction result is unavailable"));
        }
        Currency currency = accounts.findActiveOwnedAccount(accountId, ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Account was not found or is not accessible")).currency();
        if (categoryId != null && !categoryId.isBlank()) {
            TransactionCategory category = categories.findById(UUID.fromString(categoryId.trim()))
                    .filter(TransactionCategory::isActive)
                    .orElseThrow(() -> new IllegalArgumentException("Category was not found or is inactive"));
            if (category.getOwnerId() != null && !category.getOwnerId().equals(ownerId)) throw new IllegalArgumentException("Category is not accessible");
            boolean compatible = category.getKind() == TransactionCategory.Kind.BOTH
                    || (type == FinancialTransaction.TransactionType.INCOME && category.getKind() == TransactionCategory.Kind.INCOME)
                    || (type == FinancialTransaction.TransactionType.EXPENSE && category.getKind() == TransactionCategory.Kind.EXPENSE);
            if (!compatible) throw new IllegalArgumentException("Category does not match transaction type");
        }
        UUID counter = counterAccounts.counterAccount(type, currency);
        Instant now = clock.instant();
        Instant effectiveAt = occurredAt == null ? now : occurredAt;
        UUID transactionId = UUID.randomUUID();
        FinancialTransaction transaction = type == FinancialTransaction.TransactionType.INCOME
                ? postingService.recordIncome(transactionId, ownerId, accountId, counter, amount, currency, description, categoryId, effectiveAt)
                : postingService.recordExpense(transactionId, ownerId, accountId, counter, amount, currency, description, categoryId, effectiveAt);
        IdempotencyRecord keyRecord = new IdempotencyRecord(UUID.randomUUID(), ownerId, idempotencyKey.trim(), fingerprint,
                transactionId, now, now.plus(KEY_RETENTION));
        return operationStore.saveAtomically(transaction, keyRecord);
    }
}
