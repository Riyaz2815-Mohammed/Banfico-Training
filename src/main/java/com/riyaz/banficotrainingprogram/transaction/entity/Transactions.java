package com.riyaz.banficotrainingprogram.transaction.entity;

import com.riyaz.banficotrainingprogram.account.entity.Account;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "transaction_id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "transaction_type", nullable = false, length = 20)
    private String type;

    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    private Integer amount;

    @Column(name = "transaction_time", nullable = false, updatable = false)
    private LocalDateTime transactionTime;

    @Column(name = "balance_after")
    private Integer balanceAfter;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    protected Transactions() {}

    public Transactions(String type, Integer amount, LocalDateTime transactionTime, Account account, Integer balanceAfter) {
        this.type = type;
        this.amount = amount;
        this.transactionTime = transactionTime;
        this.account = account;
        this.balanceAfter = balanceAfter;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Integer getAmount() { return amount; }
    public void setAmount(Integer amount) { this.amount = amount; }
    public LocalDateTime getTransactionTime() { return transactionTime; }
    public void setTransactionTime(LocalDateTime transactionTime) { this.transactionTime = transactionTime; }
    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }
    public Integer getBalanceAfter() { return balanceAfter; }
    public void setBalanceAfter(Integer balanceAfter) { this.balanceAfter = balanceAfter; }
}
