package com.riyaz.banficotrainingprogram.dto;

import java.util.UUID;

public class BeneficiaryResponse {
    private UUID id;
    private UUID customerId;
    private UUID accountId;
    private String accountNo;
    private String accountType;
    private String accountHolderName;
    private String nickname;

    public BeneficiaryResponse(UUID id, UUID customerId, UUID accountId,
                                String accountNo, String accountType,
                                String accountHolderName, String nickname) {
        this.id = id;
        this.customerId = customerId;
        this.accountId = accountId;
        this.accountNo = accountNo;
        this.accountType = accountType;
        this.accountHolderName = accountHolderName;
        this.nickname = nickname;
    }

    public UUID getId() { return id; }
    public UUID getCustomerId() { return customerId; }
    public UUID getAccountId() { return accountId; }
    public String getAccountNo() { return accountNo; }
    public String getAccountType() { return accountType; }
    public String getAccountHolderName() { return accountHolderName; }
    public String getNickname() { return nickname; }
}
