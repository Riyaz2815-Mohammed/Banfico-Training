package com.riyaz.banficotrainingprogram.dto;

import java.util.UUID;

public class AccountLookupResponse {
    private UUID id;
    private String accountNo;
    private String accountType;
    private String accountHolderName;

    public AccountLookupResponse(UUID id, String accountNo, String accountType, String accountHolderName) {
        this.id = id;
        this.accountNo = accountNo;
        this.accountType = accountType;
        this.accountHolderName = accountHolderName;
    }

    public UUID getId() { return id; }
    public String getAccountNo() { return accountNo; }
    public String getAccountType() { return accountType; }
    public String getAccountHolderName() { return accountHolderName; }
}
