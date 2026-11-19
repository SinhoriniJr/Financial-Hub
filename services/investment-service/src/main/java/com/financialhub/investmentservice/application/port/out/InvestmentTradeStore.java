package com.financialhub.investmentservice.application.port.out;

import com.financialhub.investmentservice.domain.InvestmentPosition;
import com.financialhub.investmentservice.domain.InvestmentTrade;

/** Commits a trade and its rebuilt position projection atomically. */
public interface InvestmentTradeStore {
    InvestmentTrade saveTradeAndPosition(InvestmentTrade trade, InvestmentPosition position);
}
