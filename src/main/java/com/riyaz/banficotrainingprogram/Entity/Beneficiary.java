package com.riyaz.banficotrainingprogram.Entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "beneficiaries")
public class Beneficiary {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "beneficiary_id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "beneficiary_account_id", nullable = false)
    private Account beneficiaryAccount;

    @Column(name = "nickname", length = 50)
    private String nickname;

    protected Beneficiary() {
    }

    public Beneficiary(
            Customer customer,
            Account beneficiaryAccount,
            String nickname
    ) {
        this.customer = customer;
        this.beneficiaryAccount = beneficiaryAccount;
        this.nickname = nickname;
    }

    public UUID getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Account getBeneficiaryAccount() {
        return beneficiaryAccount;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
