package com.pavlenko.wallet_rest.controller;

import com.pavlenko.wallet_rest.entity.dto.BalanceResponse;
import com.pavlenko.wallet_rest.entity.dto.OperationRequest;
import com.pavlenko.wallet_rest.exception.InsufficientFundsException;
import com.pavlenko.wallet_rest.exception.InvalidOperationException;
import com.pavlenko.wallet_rest.exception.WalletNotFoundException;
import com.pavlenko.wallet_rest.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/api/v1")
public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/wallets/{walletId}")
    public ResponseEntity<BalanceResponse> getBalance(@PathVariable UUID walletId) {
        try {
            return ResponseEntity.ok(new BalanceResponse(walletId, walletService.getBalance(walletId)));
        } catch (WalletNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/wallet")
    public ResponseEntity<String> performOperation(@RequestBody OperationRequest request) {
        try {
            walletService.performOperation(request);
            return ResponseEntity.ok().build();
        } catch (WalletNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Кошелек не найден");
        } catch (InsufficientFundsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Недостаточно средств");
        } catch (InvalidOperationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Некорректная операция");
        }
    }
}
