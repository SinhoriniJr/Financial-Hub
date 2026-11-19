package com.financialhub.investmentservice.application.port.out;

import com.financialhub.investmentservice.domain.InvestmentPosition;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvestmentPositionRepository {
    InvestmentPosition save(InvestmentPosition position);
    Optional<InvestmentPosition> findByOwnerIdAndAccountIdAndAssetSymbol(UUID ownerId, UUID accountId, String assetSymbol);
    List<InvestmentPosition> findAllByOwnerIdAndAccountId(UUID ownerId, UUID accountId);
}
