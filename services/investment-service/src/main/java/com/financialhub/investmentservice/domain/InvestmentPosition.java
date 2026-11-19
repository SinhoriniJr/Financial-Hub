package com.financialhub.investmentservice.domain;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;
import java.util.UUID;

/** Rebuildable position projection; InvestmentTrade remains the source of history. */
public final class InvestmentPosition {
    private final UUID id;
    private final UUID ownerId;
    private final UUID investmentAccountId;
    private final String assetSymbol;
    private BigDecimal quantity;
    private BigDecimal averageCost;
    private final Currency currency;

    public InvestmentPosition(UUID id, UUID ownerId, UUID investmentAccountId, String assetSymbol, BigDecimal quantity, BigDecimal averageCost, Currency currency) {
        this.id = Objects.requireNonNull(id); this.ownerId = Objects.requireNonNull(ownerId); this.investmentAccountId = Objects.requireNonNull(investmentAccountId);
        if (assetSymbol == null || assetSymbol.isBlank()) throw new IllegalArgumentException("assetSymbol is required"); this.assetSymbol = assetSymbol.trim().toUpperCase();
        this.quantity = nonNegative(quantity, "quantity"); this.averageCost = nonNegative(averageCost, "averageCost"); this.currency = Objects.requireNonNull(currency);
    }
    public void applyBuy(BigDecimal bought, BigDecimal unitCost) { positive(bought, "bought"); positive(unitCost, "unitCost"); BigDecimal total = quantity.multiply(averageCost).add(bought.multiply(unitCost)); quantity = quantity.add(bought); averageCost = total.divide(quantity, java.math.MathContext.DECIMAL128); }
    public void applySell(BigDecimal sold) { positive(sold, "sold"); if (sold.compareTo(quantity) > 0) throw new IllegalArgumentException("Cannot sell more than current position"); quantity = quantity.subtract(sold); if (quantity.signum() == 0) averageCost = BigDecimal.ZERO; }
    private static BigDecimal nonNegative(BigDecimal v, String n) { if (v == null || v.signum() < 0) throw new IllegalArgumentException(n + " cannot be negative"); return v; }
    private static void positive(BigDecimal v, String n) { if (v == null || v.signum() <= 0) throw new IllegalArgumentException(n + " must be positive"); }
    public UUID getId() { return id; } public UUID getOwnerId() { return ownerId; } public UUID getInvestmentAccountId() { return investmentAccountId; }
    public String getAssetSymbol() { return assetSymbol; } public BigDecimal getQuantity() { return quantity; } public BigDecimal getAverageCost() { return averageCost; } public Currency getCurrency() { return currency; }
}
