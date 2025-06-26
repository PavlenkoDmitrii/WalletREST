package com.pavlenko.wallet_rest.repository;

import com.pavlenko.wallet_rest.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {
    BigDecimal getWalletById(UUID id);
}
