package com.financialhub.accountservice.interfaces.controller;

import com.financialhub.accountservice.application.usecase.OpenAccountUseCase;
import com.financialhub.accountservice.domain.FinancialAccount;
import com.financialhub.accountservice.interfaces.dto.request.OpenAccountRequest;
import com.financialhub.accountservice.interfaces.dto.response.AccountResponse;
import java.net.URI;
import java.security.Principal;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final OpenAccountUseCase openAccount;
    public AccountController(OpenAccountUseCase openAccount) { this.openAccount = openAccount; }

    @PostMapping
    public ResponseEntity<AccountResponse> open(@Valid @RequestBody OpenAccountRequest request, Principal principal) {
        var account = openAccount.execute(UUID.fromString(principal.getName()), request.name(),
                FinancialAccount.AccountType.valueOf(request.type().trim().toUpperCase(java.util.Locale.ROOT)), request.currency());
        return ResponseEntity.created(URI.create("/api/v1/accounts/" + account.getId())).body(toResponse(account));
    }
    private static AccountResponse toResponse(FinancialAccount account) {
        return new AccountResponse(account.getId(), account.getOwnerId(), account.getName(), account.getType().name(),
                account.getCurrency().getCurrencyCode(), account.getStatus().name(), account.getCreatedAt(), account.getUpdatedAt());
    }
}
