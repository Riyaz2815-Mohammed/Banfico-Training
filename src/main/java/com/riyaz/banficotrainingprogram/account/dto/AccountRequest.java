package com.riyaz.banficotrainingprogram.account.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class AccountRequest {
    private String accountType;
    private Integer balance;
    private UUID customerId;

    public AccountRequest() {}

    @NotBlank(message = "Set Account Type")
    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    @NotNull
    public UUID getCustomerId() { return customerId; }
    public void setCustomerId(UUID customerId) { this.customerId = customerId; }

    @NotNull
    public Integer getBalance() { return balance; }
    public void setBalance(Integer balance) { this.balance = balance; }
}
