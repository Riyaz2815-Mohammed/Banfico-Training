package com.riyaz.banficotrainingprogram.dto;

import java.util.UUID;

public class AccountResponse {
    private UUID id;
    private String accountNo;
    private String accountType;
    private Integer balance;
    private UUID customerId;

    public AccountResponse(UUID id, String accountNo, String accountType, Integer balance, UUID customerId) {
        this.id = id;
        this.accountNo = accountNo;
        this.accountType = accountType;
        this.balance = balance;
        this.customerId = customerId;
    }

    public UUID getId() {
        return id;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public String getAccountType() {
        return accountType;
    }

    public Integer getBalance() {
        return balance;
    }

    public UUID getCustomerId() {
        return customerId;
    }
}
