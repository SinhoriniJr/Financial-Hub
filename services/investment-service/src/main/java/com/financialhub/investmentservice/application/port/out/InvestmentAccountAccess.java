package com.financialhub.investmentservice.application.port.out;

import java.util.Currency;
import java.util.Optional;
import java.util.UUID;

public interface InvestmentAccountAccess {
    Optional<AccountSnapshot> findActiveOwnedInvestmentAccount(UUID accountId, UUID ownerId);
    record AccountSnapshot(UUID accountId, UUID ownerId, Currency currency) { }
}
