package com.financialhub.accountservice.application.port.out;

import com.financialhub.accountservice.domain.FinancialAccount;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FinancialAccountRepository {
    FinancialAccount save(FinancialAccount account);
    Optional<FinancialAccount> findByIdAndOwnerId(UUID accountId, UUID ownerId);
    List<FinancialAccount> findAllByOwnerId(UUID ownerId);
    boolean existsByIdAndOwnerId(UUID accountId, UUID ownerId);
}
