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

    public TransactionResponse(UUID id, String transactionType, UUID accountId, Integer balance, Integer amount, LocalDateTime timestamp) {
        this.id = id;
        this.transactionType = transactionType;
        this.accountId = accountId;
        this.balance = balance;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public UUID getId() { return id; }
    public UUID getAccountId() { return accountId; }
    public String getTransactionType() { return transactionType; }
    public Integer getAmount() { return amount; }
    public Integer getBalance() { return balance; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
