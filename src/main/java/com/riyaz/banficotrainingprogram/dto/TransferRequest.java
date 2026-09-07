package com.riyaz.banficotrainingprogram.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public class TransferRequest {
    private UUID fromAccountId;
    private String recipientAccountNo;
    private Integer amount;
    private String note;

    public TransferRequest() {}

    @NotNull(message = "Source account is required")
    public UUID getFromAccountId() { return fromAccountId; }
    public void setFromAccountId(UUID fromAccountId) { this.fromAccountId = fromAccountId; }

    @NotBlank(message = "Recipient account number is required")
    public String getRecipientAccountNo() { return recipientAccountNo; }
    public void setRecipientAccountNo(String recipientAccountNo) { this.recipientAccountNo = recipientAccountNo; }

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    public Integer getAmount() { return amount; }
    public void setAmount(Integer amount) { this.amount = amount; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
