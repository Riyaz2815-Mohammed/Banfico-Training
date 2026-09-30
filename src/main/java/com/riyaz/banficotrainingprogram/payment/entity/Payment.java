package com.riyaz.banficotrainingprogram.payment.entity;

import com.riyaz.banficotrainingprogram.account.entity.Account;
import com.riyaz.banficotrainingprogram.customer.entity.Customer;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @Column(name = "payment_id", nullable = false, updatable = false)
    private UUID paymentId;

    @ManyToOne
    @JoinColumn(name = "from_account_id", nullable = false)
    private Account fromAccount;

    @Column(name = "to_account_no", nullable = false)
    private String toAccountNo;

    @Column(name = "to_account_name", nullable = false)
    private String toAccountName;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "note")
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private PaymentStatus status;

    @ManyToOne
    @JoinColumn(name = "initiated_by", nullable = false)
    private Customer initiatedBy;

    @Column(name = "initiated_at", nullable = false, updatable = false)
    private LocalDateTime initiatedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "failure_reason")
    private String failureReason;

    protected Payment() {}

    public Payment(UUID paymentId, Account fromAccount, String toAccountNo, String toAccountName, Integer amount, String note, Customer initiatedBy) {
        this.paymentId = paymentId;
        this.fromAccount = fromAccount;
        this.toAccountNo = toAccountNo;
        this.toAccountName = toAccountName;
        this.amount = amount;
        this.note = note;
        this.initiatedBy = initiatedBy;
        this.status = PaymentStatus.PENDING;
        this.initiatedAt = LocalDateTime.now();
    }

    public UUID getPaymentId() { return paymentId; }
    public Account getFromAccount() { return fromAccount; }
    public String getToAccountNo() { return toAccountNo; }
    public String getToAccountName() { return toAccountName; }
    public Integer getAmount() { return amount; }
    public String getNote() { return note; }
    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }
    public Customer getInitiatedBy() { return initiatedBy; }
    public LocalDateTime getInitiatedAt() { return initiatedAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }
}
