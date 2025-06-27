package com.pavlenko.wallet_rest.service;

import com.pavlenko.wallet_rest.dto.OperationRequest;

import java.math.BigDecimal;
import java.util.UUID;

public interface WalletService {

    BigDecimal getBalance(UUID walletId);

    void performOperation(OperationRequest request);

}
