package com.riyaz.banficotrainingprogram.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public class AccountRequest {
    private String accountNo;
    private String accountType;
//    private Integer balance;
    private UUID customerId;

    public AccountRequest() {}

    @NotBlank(message = "Account No is Missing")
    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    @NotBlank(message = "Set Account Type")
    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @NotBlank
    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

//    @NotBlank
//    public Integer getBalance() {
//        return balance;
//    }
//
//    public void setBalance(Integer balance) {
//        this.balance = balance;
//    }
}
