package com.financialhub.ledgerservice.application.port.out;

import com.financialhub.ledgerservice.domain.FinancialTransaction;
import java.util.Currency;
import java.util.UUID;

/** Resolves the internal balancing account used for income or expense postings. */
public interface LedgerCounterAccountAccess {
    UUID counterAccount(FinancialTransaction.TransactionType type, Currency currency);
}
