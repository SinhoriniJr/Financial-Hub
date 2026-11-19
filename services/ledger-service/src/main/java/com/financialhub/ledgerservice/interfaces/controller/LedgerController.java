package com.financialhub.ledgerservice.interfaces.controller;

import com.financialhub.ledgerservice.application.usecase.RecordAccountTransactionUseCase;
import com.financialhub.ledgerservice.application.usecase.RecordTransferUseCase;
import com.financialhub.ledgerservice.domain.FinancialTransaction;
import com.financialhub.ledgerservice.interfaces.dto.request.CreateTransactionRequest;
import com.financialhub.ledgerservice.interfaces.dto.request.CreateTransferRequest;
import com.financialhub.ledgerservice.interfaces.dto.response.TransactionResponse;
import java.security.Principal;
import java.util.Currency;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/ledger")
public class LedgerController {
    private final RecordAccountTransactionUseCase recordTransaction;
    private final RecordTransferUseCase recordTransfer;
    public LedgerController(RecordAccountTransactionUseCase recordTransaction, RecordTransferUseCase recordTransfer) {
        this.recordTransaction = recordTransaction; this.recordTransfer = recordTransfer;
    }

    @PostMapping("/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse record(@Valid @RequestBody CreateTransactionRequest request,
            @RequestHeader("Idempotency-Key") String idempotencyKey, Principal principal) {
        var transaction = recordTransaction.execute(UUID.fromString(principal.getName()), request.accountId(),
                FinancialTransaction.TransactionType.valueOf(request.type().trim().toUpperCase(java.util.Locale.ROOT)),
                request.amount(), Currency.getInstance(request.currency().trim().toUpperCase(java.util.Locale.ROOT)),
                request.description(), request.categoryId(), request.occurredAt(), idempotencyKey);
        return toResponse(transaction);
    }

    @PostMapping("/transfers")
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse transfer(@Valid @RequestBody CreateTransferRequest request,
            @RequestHeader("Idempotency-Key") String idempotencyKey, Principal principal) {
        var transaction = recordTransfer.execute(UUID.fromString(principal.getName()), request.sourceAccountId(),
                request.destinationAccountId(), request.amount(),
                Currency.getInstance(request.currency().trim().toUpperCase(java.util.Locale.ROOT)),
                request.description(), request.occurredAt(), idempotencyKey);
        return toResponse(transaction);
    }

    private static TransactionResponse toResponse(FinancialTransaction transaction) {
        var entries = transaction.getEntries().stream().map(entry -> new TransactionResponse.EntryResponse(
                entry.accountId(), entry.direction().name(), entry.amount(), entry.currency().getCurrencyCode())).toList();
        return new TransactionResponse(transaction.getId(), transaction.getType().name(), transaction.getDescription(),
                transaction.getCategoryId(), transaction.getOccurredAt(), entries, transaction.getReversesTransactionId());
    }
}
