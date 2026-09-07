package com.riyaz.banficotrainingprogram.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class TransferResponse {
    private UUID referenceId;
    private String fromAccountNo;
    private Integer remainingBalance;
    private String recipientName;
    private String recipientAccountNo;
    private Integer amount;
    private String note;
    private LocalDateTime timestamp;

    public TransferResponse(UUID referenceId, String fromAccountNo, Integer remainingBalance,
                            String recipientName, String recipientAccountNo,
                            Integer amount, String note, LocalDateTime timestamp) {
        this.referenceId = referenceId;
        this.fromAccountNo = fromAccountNo;
        this.remainingBalance = remainingBalance;
        this.recipientName = recipientName;
        this.recipientAccountNo = recipientAccountNo;
        this.amount = amount;
        this.note = note;
        this.timestamp = timestamp;
    }

    public UUID getReferenceId() { return referenceId; }
    public String getFromAccountNo() { return fromAccountNo; }
    public Integer getRemainingBalance() { return remainingBalance; }
    public String getRecipientName() { return recipientName; }
    public String getRecipientAccountNo() { return recipientAccountNo; }
    public Integer getAmount() { return amount; }
    public String getNote() { return note; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
