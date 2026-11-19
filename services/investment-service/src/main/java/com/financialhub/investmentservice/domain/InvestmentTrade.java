package com.financialhub.investmentservice.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.Objects;
import java.util.UUID;

public final class InvestmentTrade {
    private final UUID id;
    private final UUID ownerId;
    private final UUID investmentAccountId;
    private final String assetSymbol;
    private final TradeType type;
    private final BigDecimal quantity;
    private final BigDecimal unitPrice;
    private final BigDecimal fees;
    private final Currency currency;
    private final Instant executedAt;

    public InvestmentTrade(UUID id, UUID ownerId, UUID investmentAccountId, String assetSymbol, TradeType type,
                           BigDecimal quantity, BigDecimal unitPrice, BigDecimal fees, Currency currency, Instant executedAt) {
        this.id = Objects.requireNonNull(id); this.ownerId = Objects.requireNonNull(ownerId); this.investmentAccountId = Objects.requireNonNull(investmentAccountId);
        if (assetSymbol == null || assetSymbol.isBlank()) throw new IllegalArgumentException("assetSymbol is required"); this.assetSymbol = assetSymbol.trim().toUpperCase(java.util.Locale.ROOT);
        this.type = Objects.requireNonNull(type); this.quantity = positive(quantity, "quantity"); this.unitPrice = positive(unitPrice, "unitPrice");
        this.fees = Objects.requireNonNull(fees); if (fees.signum() < 0) throw new IllegalArgumentException("fees cannot be negative");
        this.currency = Objects.requireNonNull(currency); this.executedAt = Objects.requireNonNull(executedAt);
    }
    private static BigDecimal positive(BigDecimal v, String n) { if (v == null || v.signum() <= 0) throw new IllegalArgumentException(n + " must be positive"); return v; }
    public UUID getId() { return id; } public UUID getOwnerId() { return ownerId; } public UUID getInvestmentAccountId() { return investmentAccountId; }
    public String getAssetSymbol() { return assetSymbol; } public TradeType getType() { return type; } public BigDecimal getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; } public BigDecimal getFees() { return fees; } public Currency getCurrency() { return currency; } public Instant getExecutedAt() { return executedAt; }
    public enum TradeType { BUY, SELL }
}
