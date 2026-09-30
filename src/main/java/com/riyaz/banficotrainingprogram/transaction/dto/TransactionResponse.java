package com.riyaz.banficotrainingprogram.transaction.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class TransactionResponse {
    private UUID id;
    private UUID accountId;
    private String transactionType;
    private Integer amount;
    private Integer balance;
    private LocalDateTime timestamp;
    private String description;

    public TransactionResponse(UUID id, String transactionType, UUID accountId, Integer balance, Integer amount, LocalDateTime timestamp, String description) {
        this.id = id;
        this.transactionType = transactionType;
        this.accountId = accountId;
        this.balance = balance;
        this.amount = amount;
        this.timestamp = timestamp;
        this.description = description;
    }

    public UUID getId() { return id; }
    public UUID getAccountId() { return accountId; }
    public String getTransactionType() { return transactionType; }
    public Integer getAmount() { return amount; }
    public Integer getBalance() { return balance; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getDescription() { return description; }
}
