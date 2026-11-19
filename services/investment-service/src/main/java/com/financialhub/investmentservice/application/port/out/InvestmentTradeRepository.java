package com.financialhub.investmentservice.application.port.out;

import com.financialhub.investmentservice.domain.InvestmentTrade;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface InvestmentTradeRepository {
    InvestmentTrade save(InvestmentTrade trade);
    List<InvestmentTrade> findByOwnerIdAndAccountIdAndExecutedAtBetween(UUID ownerId, UUID accountId, Instant from, Instant to);
    boolean existsByIdAndOwnerId(UUID tradeId, UUID ownerId);
}
