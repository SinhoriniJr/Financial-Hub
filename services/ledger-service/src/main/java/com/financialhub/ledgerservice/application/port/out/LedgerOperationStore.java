package com.financialhub.ledgerservice.application.port.out;

import com.financialhub.ledgerservice.domain.FinancialTransaction;
import com.financialhub.ledgerservice.domain.IdempotencyRecord;

/** Persists the ledger operation, idempotency record and outbox event in one local database transaction. */
public interface LedgerOperationStore {
    FinancialTransaction saveAtomically(FinancialTransaction transaction, IdempotencyRecord idempotencyRecord);
}
