package com.pavlenko.wallet_rest;

import com.pavlenko.wallet_rest.controller.WalletController;
import com.pavlenko.wallet_rest.dto.OperationRequest;
import com.pavlenko.wallet_rest.entity.OperationType;
import com.pavlenko.wallet_rest.exception_handling.InsufficientFundsException;
import com.pavlenko.wallet_rest.exception_handling.WalletNotFoundException;
import com.pavlenko.wallet_rest.service.WalletService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WalletController.class)
public class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletService walletService;

    private final UUID walletId = UUID.randomUUID();

    @Test
    public void testGetBalanceSuccess() throws Exception {
        BigDecimal balance = BigDecimal.valueOf(100.50);
        Mockito.when(walletService.getBalance(walletId)).thenReturn(balance);

        mockMvc.perform(get("/api/v1/wallets/{walletId}", walletId))
                .andExpect(status().isOk())
                .andExpect(content().string(balance.toString()));
    }

    @Test
    public void testGetBalanceWalletNotFound() throws Exception {
        Mockito.when(walletService.getBalance(walletId)).thenThrow(new WalletNotFoundException());

        mockMvc.perform(get("/api/v1/wallets/{walletId}", walletId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testPerformOperationDeposit() throws Exception {
        OperationRequest request = new OperationRequest();
        request.setWalletId(walletId);
        request.setOperation(OperationType.DEPOSIT);
        request.setAmount(BigDecimal.valueOf(50));

        String json = "{ " +
                "\"walletId\": \"" + walletId + "\", " +
                "\"operation\": \"DEPOSIT\", " +
                "\"amount\": 50 " +
                "}";

        Mockito.doNothing().when(walletService).performOperation(Mockito.any());
        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void testPerformOperationWithdrawInsufficientFunds() throws Exception {
        OperationRequest request = new OperationRequest();
        request.setWalletId(walletId);
        request.setOperation(OperationType.WITHDRAW);
        request.setAmount(BigDecimal.valueOf(200));
        String json = "{ " +
                "\"walletId\": \"" + walletId + "\", " +
                "\"operation\": \"WITHDRAW\", " +
                "\"amount\": 200 }";
        Mockito.doThrow(new InsufficientFundsException())
                .when(walletService).performOperation(Mockito.any());

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }
}
