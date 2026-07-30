package com.riyaz.banficotrainingprogram.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class BeneficiaryRequest {
    private UUID accountId;
    private String nickname;

    public BeneficiaryRequest() {}

    @NotNull(message = "Account ID is missing")
    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    @NotBlank(message = "Nickname is missing")
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
