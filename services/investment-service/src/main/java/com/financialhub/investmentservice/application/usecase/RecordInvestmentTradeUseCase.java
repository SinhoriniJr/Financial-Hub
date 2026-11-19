package com.financialhub.investmentservice.application.usecase;

import com.financialhub.investmentservice.application.port.out.InvestmentAccountAccess;
import com.financialhub.investmentservice.application.port.out.InvestmentPositionRepository;
import com.financialhub.investmentservice.application.port.out.InvestmentTradeRepository;
import com.financialhub.investmentservice.application.port.out.InvestmentTradeStore;
import com.financialhub.investmentservice.domain.InvestmentPosition;
import com.financialhub.investmentservice.domain.InvestmentTrade;
import com.financialhub.investmentservice.domain.service.InvestmentPositionCalculator;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.Currency;
import java.util.Objects;
import java.util.UUID;

public final class RecordInvestmentTradeUseCase {
    private final InvestmentAccountAccess accountAccess;
    private final InvestmentTradeRepository trades;
    private final InvestmentPositionRepository positions;
    private final InvestmentTradeStore tradeStore;
    private final InvestmentPositionCalculator calculator;
    private final Clock clock;

    public RecordInvestmentTradeUseCase(InvestmentAccountAccess accountAccess, InvestmentTradeRepository trades,
            InvestmentPositionRepository positions, InvestmentTradeStore tradeStore,
            InvestmentPositionCalculator calculator, Clock clock) {
        this.accountAccess = Objects.requireNonNull(accountAccess); this.trades = Objects.requireNonNull(trades);
        this.positions = Objects.requireNonNull(positions); this.tradeStore = Objects.requireNonNull(tradeStore);
        this.calculator = Objects.requireNonNull(calculator); this.clock = Objects.requireNonNull(clock);
    }

    public InvestmentTrade execute(UUID ownerId, UUID accountId, String symbol, InvestmentTrade.TradeType type,
            BigDecimal quantity, BigDecimal unitPrice, BigDecimal fees, Currency currency, Instant executedAt) {
        var account = accountAccess.findActiveOwnedInvestmentAccount(accountId, ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Investment account was not found or is not accessible"));
        if (!account.currency().equals(currency)) throw new IllegalArgumentException("Trade currency must match investment account currency");
        var trade = new InvestmentTrade(UUID.randomUUID(), ownerId, accountId, symbol, type, quantity, unitPrice,
                fees == null ? BigDecimal.ZERO : fees, currency, executedAt == null ? clock.instant() : executedAt);
        var history = new java.util.ArrayList<>(trades.findAllByOwnerIdAndAccountIdAndAssetSymbol(
                ownerId, accountId, trade.getAssetSymbol()));
        history.add(trade);
        var existing = positions.findByOwnerIdAndAccountIdAndAssetSymbol(ownerId, accountId, trade.getAssetSymbol());
        var position = calculator.calculate(existing.map(InvestmentPosition::getId).orElseGet(UUID::randomUUID),
                ownerId, accountId, trade.getAssetSymbol(), currency, history);
        return tradeStore.saveTradeAndPosition(trade, position);
    }
}
