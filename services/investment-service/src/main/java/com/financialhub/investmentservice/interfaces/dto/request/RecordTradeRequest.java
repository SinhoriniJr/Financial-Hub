package com.financialhub.investmentservice.interfaces.dto.request;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record RecordTradeRequest(UUID investmentAccountId, String assetSymbol, String type,
                                 BigDecimal quantity, BigDecimal unitPrice, BigDecimal fees,
                                 String currency, Instant executedAt) { }
