package com.pavlenko.wallet_rest.dto;

import com.pavlenko.wallet_rest.entity.OperationType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public class OperationRequest {
    @NotNull(message = "walletId не может быть null")
    private UUID walletId;
    @NotNull(message = "operationType не может быть null")
    private OperationType operation;
    @NotNull
    @DecimalMin(value = "0.01", message = "amount должен быть больше \"0.01\"")
    private BigDecimal amount;

    public UUID getWalletId() {
        return walletId;
    }

    public void setWalletId(UUID walletId) {
        this.walletId = walletId;
    }

    public OperationType getOperation() {
        return operation;
    }

    public void setOperation(OperationType operation) {
        this.operation = operation;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
