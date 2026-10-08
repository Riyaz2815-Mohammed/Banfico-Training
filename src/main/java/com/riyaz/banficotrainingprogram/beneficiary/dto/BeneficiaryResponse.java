package com.riyaz.banficotrainingprogram.beneficiary.dto;

import java.util.UUID;

public class BeneficiaryResponse {
    private UUID id;
    private UUID customerId;
    private UUID accountId;
    private String accountNo;
    private String accountType;
    private String accountHolderName;
    private String nickname;
    private UUID sourceAccountId;
    private String sourceAccountNo;

    public BeneficiaryResponse(UUID id, UUID customerId, UUID accountId, String accountNo, String accountType, String accountHolderName, String nickname, UUID sourceAccountId, String sourceAccountNo) {
        this.id = id;
        this.customerId = customerId;
        this.accountId = accountId;
        this.accountNo = accountNo;
        this.accountType = accountType;
        this.accountHolderName = accountHolderName;
        this.nickname = nickname;
        this.sourceAccountId = sourceAccountId;
        this.sourceAccountNo = sourceAccountNo;
    }

    public UUID getId() { return id; }
    public UUID getCustomerId() { return customerId; }
    public UUID getAccountId() { return accountId; }
    public String getAccountNo() { return accountNo; }
    public String getAccountType() { return accountType; }
    public String getAccountHolderName() { return accountHolderName; }
    public String getNickname() { return nickname; }
    public UUID getSourceAccountId() { return sourceAccountId; }
    public String getSourceAccountNo() { return sourceAccountNo; }
}
