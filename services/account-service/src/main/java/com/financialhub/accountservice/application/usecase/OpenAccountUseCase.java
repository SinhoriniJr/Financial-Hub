package com.financialhub.accountservice.application.usecase;

import com.financialhub.accountservice.application.port.out.FinancialAccountRepository;
import com.financialhub.accountservice.domain.FinancialAccount;
import java.time.Clock;
import java.util.Currency;
import java.util.Objects;
import java.util.UUID;

public final class OpenAccountUseCase {
    private final FinancialAccountRepository accounts;
    private final Clock clock;

    public OpenAccountUseCase(FinancialAccountRepository accounts, Clock clock) { this.accounts = Objects.requireNonNull(accounts); this.clock = Objects.requireNonNull(clock); }
    public FinancialAccount execute(UUID authenticatedUserId, String name, FinancialAccount.AccountType type, String currencyCode) {
        Objects.requireNonNull(authenticatedUserId); Objects.requireNonNull(type);
        Currency currency;
        try { currency = Currency.getInstance(Objects.requireNonNull(currencyCode).trim().toUpperCase(java.util.Locale.ROOT)); }
        catch (RuntimeException ex) { throw new IllegalArgumentException("Unsupported currency", ex); }
        return accounts.save(FinancialAccount.open(authenticatedUserId, name, type, currency, clock.instant()));
    }
}
