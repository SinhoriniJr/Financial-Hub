package com.financialhub.investmentservice.domain.service;

import com.financialhub.investmentservice.domain.InvestmentPosition;
import com.financialhub.investmentservice.domain.InvestmentTrade;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/** Rebuilds the weighted-average-cost position from the immutable trade history. */
public final class InvestmentPositionCalculator {
    public InvestmentPosition calculate(UUID positionId, UUID ownerId, UUID accountId, String assetSymbol,
                                        Currency currency, List<InvestmentTrade> trades) {
        Objects.requireNonNull(trades);
        String normalizedSymbol = Objects.requireNonNull(assetSymbol).trim().toUpperCase(java.util.Locale.ROOT);
        InvestmentPosition position = new InvestmentPosition(positionId, ownerId, accountId, normalizedSymbol,
                BigDecimal.ZERO, BigDecimal.ZERO, currency);
        for (InvestmentTrade trade : trades.stream().sorted(Comparator.comparing(InvestmentTrade::getExecutedAt)
                .thenComparing(InvestmentTrade::getId)).toList()) {
            if (!trade.getOwnerId().equals(ownerId) || !trade.getInvestmentAccountId().equals(accountId)
                    || !trade.getAssetSymbol().equals(normalizedSymbol) || !trade.getCurrency().equals(currency)) {
                throw new IllegalArgumentException("Trade does not belong to the position being calculated");
            }
            if (trade.getType() == InvestmentTrade.TradeType.BUY) {
                BigDecimal feesPerUnit = trade.getFees().divide(trade.getQuantity(), java.math.MathContext.DECIMAL128);
                position.applyBuy(trade.getQuantity(), trade.getUnitPrice().add(feesPerUnit));
            } else {
                position.applySell(trade.getQuantity());
            }
        }
        return position;
    }
}
