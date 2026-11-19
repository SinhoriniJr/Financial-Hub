package com.financialhub.investmentservice.domain;

import java.util.Currency;
import java.util.Objects;

/** Instrument metadata; market prices are external observations, not mutable trade history. */
public record InvestmentAsset(String symbol, String name, AssetClass assetClass, Currency quoteCurrency, boolean active) {
    public InvestmentAsset {
        if (symbol == null || symbol.isBlank()) throw new IllegalArgumentException("symbol is required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name is required");
        symbol = symbol.trim().toUpperCase(java.util.Locale.ROOT); name = name.trim();
        Objects.requireNonNull(assetClass); Objects.requireNonNull(quoteCurrency);
    }
    public enum AssetClass { STOCK, ETF, BOND, FUND, CRYPTO, OTHER }
}
