package com.pavlenko.wallet_rest.controller;

import com.pavlenko.wallet_rest.dto.BalanceResponse;
import com.pavlenko.wallet_rest.dto.OperationRequest;
import com.pavlenko.wallet_rest.service.WalletService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/wallets/{walletId}")
    public ResponseEntity<BigDecimal> getBalance(@Valid @PathVariable UUID walletId) {
        BigDecimal balance = walletService.getBalance(walletId);
        return ResponseEntity.ok(balance);
    }

    @PostMapping("/wallet")
    public ResponseEntity<Void> performOperation(@Valid @RequestBody OperationRequest request) {
        walletService.performOperation(request);
        return ResponseEntity.ok().build();
    }
}