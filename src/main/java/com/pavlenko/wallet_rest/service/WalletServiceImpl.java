package com.pavlenko.wallet_rest.service;

import com.pavlenko.wallet_rest.entity.Wallet;
import com.pavlenko.wallet_rest.entity.dto.OperationRequest;
import com.pavlenko.wallet_rest.exception.InsufficientFundsException;
import com.pavlenko.wallet_rest.exception.InvalidOperationException;
import com.pavlenko.wallet_rest.exception.WalletNotFoundException;
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
        if (request.getOperation().equalsIgnoreCase("DEPOSIT")) {
            wallet.setBalance(wallet.getBalance().add(request.getAmount()));
        } else if (request.getOperation().equalsIgnoreCase("WITHDRAW")) {
            if (wallet.getBalance().compareTo(request.getAmount()) < 0) {
                throw new InsufficientFundsException();
            }
            wallet.setBalance(wallet.getBalance().subtract(request.getAmount()));
        } else {
            throw new InvalidOperationException();
        }
        walletRepository.save(wallet);
    }
}
