package com.riyaz.banficotrainingprogram.transaction.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public class TransactionRequest {
    private String type;
    private Integer amount;
    private UUID accountId;

    public TransactionRequest() {}

    @NotBlank(message = "Transaction type is required")
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    public Integer getAmount() { return amount; }
    public void setAmount(Integer amount) { this.amount = amount; }

    @NotNull(message = "Account ID is required")
    public UUID getAccountId() { return accountId; }
    public void setAccountId(UUID accountId) { this.accountId = accountId; }
}
