package com.riyaz.banficotrainingprogram.transaction.dto;

import java.time.LocalDateTime;

public class TransferPreviewResponse {
    private String fromAccountNo;
    private Integer fromAccountBalance;
    private String toAccountNo;
    private String toAccountName;
    private Integer amount;
    private LocalDateTime estimatedAt;

    public TransferPreviewResponse(String fromAccountNo, Integer fromAccountBalance, String toAccountNo, String toAccountName, Integer amount, LocalDateTime estimatedAt) {
        this.fromAccountNo = fromAccountNo;
        this.fromAccountBalance = fromAccountBalance;
        this.toAccountNo = toAccountNo;
        this.toAccountName = toAccountName;
        this.amount = amount;
        this.estimatedAt = estimatedAt;
    }

    public String getFromAccountNo() { return fromAccountNo; }
    public Integer getFromAccountBalance() { return fromAccountBalance; }
    public String getToAccountNo() { return toAccountNo; }
    public String getToAccountName() { return toAccountName; }
    public Integer getAmount() { return amount; }
    public LocalDateTime getEstimatedAt() { return estimatedAt; }
}
