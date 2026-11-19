package com.financialhub.ledgerservice.application.port.out;

import java.util.Currency;
import java.util.Optional;
import java.util.UUID;

/** Remote account lookup port; implementations call Account Service and enforce active ownership. */
public interface LedgerAccountAccess {
    Optional<AccountSnapshot> findActiveOwnedAccount(UUID accountId, UUID ownerId);
    record AccountSnapshot(UUID accountId, UUID ownerId, Currency currency) { }
}
