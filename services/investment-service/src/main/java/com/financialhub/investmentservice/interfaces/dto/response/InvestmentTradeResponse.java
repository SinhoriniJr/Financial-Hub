package com.financialhub.investmentservice.interfaces.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record InvestmentTradeResponse(UUID id, UUID investmentAccountId, String assetSymbol, String type,
                                      BigDecimal quantity, BigDecimal unitPrice, BigDecimal fees,
                                      String currency, Instant executedAt) { }
