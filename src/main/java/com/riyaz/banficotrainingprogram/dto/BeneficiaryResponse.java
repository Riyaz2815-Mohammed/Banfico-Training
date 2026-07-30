package com.riyaz.banficotrainingprogram.dto;

import java.util.UUID;

public class BeneficiaryResponse {
    private UUID id;
    private UUID customerId;
    private UUID accountId;
    private String nickname;

    public BeneficiaryResponse(UUID id, UUID customerId, UUID accountId, String nickname) {
        this.id = id;
        this.customerId = customerId;
        this.accountId = accountId;
        this.nickname = nickname;
    }

    public UUID getId() {
        return id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public String getNickname() {
        return nickname;
    }
}
