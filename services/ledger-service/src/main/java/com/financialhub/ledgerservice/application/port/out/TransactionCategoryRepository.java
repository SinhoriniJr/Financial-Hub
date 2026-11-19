package com.financialhub.ledgerservice.application.port.out;

import com.financialhub.ledgerservice.domain.TransactionCategory;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionCategoryRepository {
    TransactionCategory save(TransactionCategory category);
    Optional<TransactionCategory> findById(UUID categoryId);
    List<TransactionCategory> findAllByOwnerIdOrSystem(UUID ownerId);
}
