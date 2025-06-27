package com.pavlenko.wallet_rest.service;

import com.pavlenko.wallet_rest.entity.Wallet;
import com.pavlenko.wallet_rest.dto.OperationRequest;
import com.pavlenko.wallet_rest.exception_handling.InsufficientFundsException;
import com.pavlenko.wallet_rest.exception_handling.InvalidOperationException;
import com.pavlenko.wallet_rest.exception_handling.WalletNotFoundException;
import com.pavlenko.wallet_rest.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    public WalletServiceImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getBalance(UUID walletId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(WalletNotFoundException::new);
        return wallet.getBalance();
    }

    @Override
    @Transactional
    public void performOperation(OperationRequest request) {
        Wallet wallet = walletRepository.findById(request.getWalletId())
                .orElseThrow(WalletNotFoundException::new);

        switch (request.getOperation()) {
            case DEPOSIT -> wallet.setBalance(wallet.getBalance().add(request.getAmount()));
            case WITHDRAW -> {
                if (wallet.getBalance().compareTo(request.getAmount()) < 0) {
                    throw new InsufficientFundsException();
                }
                wallet.setBalance(wallet.getBalance().subtract(request.getAmount()));
            }
            default -> throw new InvalidOperationException();
        }
        walletRepository.save(wallet);
    }
}
