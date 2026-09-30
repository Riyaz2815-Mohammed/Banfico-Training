package com.riyaz.banficotrainingprogram.payment.dto;

import com.riyaz.banficotrainingprogram.payment.entity.PaymentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class PaymentResponse {
    private UUID paymentId;
    private String fromAccountNo;
    private String toAccountNo;
    private String toAccountName;
    private Integer amount;
    private String note;
    private PaymentStatus status;
    private String initiatedByName;
    private LocalDateTime initiatedAt;
    private LocalDateTime completedAt;
    private String failureReason;

    public PaymentResponse(UUID paymentId, String fromAccountNo, String toAccountNo, String toAccountName, Integer amount, String note, PaymentStatus status, String initiatedByName, LocalDateTime initiatedAt, LocalDateTime completedAt, String failureReason) {
        this.paymentId = paymentId;
        this.fromAccountNo = fromAccountNo;
        this.toAccountNo = toAccountNo;
        this.toAccountName = toAccountName;
        this.amount = amount;
        this.note = note;
        this.status = status;
        this.initiatedByName = initiatedByName;
        this.initiatedAt = initiatedAt;
        this.completedAt = completedAt;
        this.failureReason = failureReason;
    }

    public UUID getPaymentId() { return paymentId; }
    public String getFromAccountNo() { return fromAccountNo; }
    public String getToAccountNo() { return toAccountNo; }
    public String getToAccountName() { return toAccountName; }
    public Integer getAmount() { return amount; }
    public String getNote() { return note; }
    public PaymentStatus getStatus() { return status; }
    public String getInitiatedByName() { return initiatedByName; }
    public LocalDateTime getInitiatedAt() { return initiatedAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public String getFailureReason() { return failureReason; }
}
