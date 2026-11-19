package com.financialhub.investmentservice.interfaces.controller;

import com.financialhub.investmentservice.application.usecase.RecordInvestmentTradeUseCase;
import com.financialhub.investmentservice.interfaces.dto.request.RecordTradeRequest;
import com.financialhub.investmentservice.interfaces.dto.response.InvestmentTradeResponse;
import java.security.Principal;
import java.util.Currency;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/investments/trades")
public class InvestmentController {
    private final RecordInvestmentTradeUseCase recordTrade;
    public InvestmentController(RecordInvestmentTradeUseCase recordTrade) { this.recordTrade = recordTrade; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InvestmentTradeResponse record(@Valid @RequestBody RecordTradeRequest request, Principal principal) {
        var trade = recordTrade.execute(UUID.fromString(principal.getName()), request.investmentAccountId(), request.assetSymbol(),
                com.financialhub.investmentservice.domain.InvestmentTrade.TradeType.valueOf(request.type().trim().toUpperCase(java.util.Locale.ROOT)),
                request.quantity(), request.unitPrice(), request.fees(),
                Currency.getInstance(request.currency().trim().toUpperCase(java.util.Locale.ROOT)), request.executedAt());
        return new InvestmentTradeResponse(trade.getId(), trade.getInvestmentAccountId(), trade.getAssetSymbol(), trade.getType().name(),
                trade.getQuantity(), trade.getUnitPrice(), trade.getFees(), trade.getCurrency().getCurrencyCode(), trade.getExecutedAt());
    }
}
