package com.financialhub.investmentservice.interfaces.dto.response;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

public record InvestmentPositionResponse(UUID id, UUID investmentAccountId, String assetSymbol,
                                         BigDecimal quantity, BigDecimal averageCost, Currency currency) { }
