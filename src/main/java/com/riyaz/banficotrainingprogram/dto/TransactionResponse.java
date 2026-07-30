package com.riyaz.banficotrainingprogram.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class TransactionResponse {
    private UUID id;
    private UUID customer_id;
    private String accountType;
    private Integer amount;
    private Integer balance;
    private LocalDateTime timestamp;

    public TransactionResponse(UUID id, String accountType, UUID customer_id, Integer balance, Integer amount, LocalDateTime timestamp) {
        this.id = id;
        this.accountType = accountType;
        this.customer_id = customer_id;
        this.balance = balance;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public UUID getId() {
        return id;
    }

    public UUID getCustomer_id() {
        return customer_id;
    }

    public String getAccountType() {
        return accountType;
    }

    public Integer getAmount() {
        return amount;
    }

    public Integer getBalance() {
        return balance;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
