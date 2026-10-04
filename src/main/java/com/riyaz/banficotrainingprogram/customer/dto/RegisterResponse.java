package com.riyaz.banficotrainingprogram.customer.dto;

import java.util.UUID;

public class RegisterResponse {
    private UUID customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String pan;
    private String phoneNumber;
    private String keycloakUserId;
    private UUID accountId;
    private String accountNo;
    private String accountType;
    private Integer balance;

    public RegisterResponse(UUID customerId, String firstName, String lastName, String email, String pan, String phoneNumber, String keycloakUserId, UUID accountId, String accountNo, String accountType, Integer balance) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.pan = pan;
        this.phoneNumber = phoneNumber;
        this.keycloakUserId = keycloakUserId;
        this.accountId = accountId;
        this.accountNo = accountNo;
        this.accountType = accountType;
        this.balance = balance;
    }

    public UUID getCustomerId() { return customerId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPan() { return pan; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getKeycloakUserId() { return keycloakUserId; }
    public UUID getAccountId() { return accountId; }
    public String getAccountNo() { return accountNo; }
    public String getAccountType() { return accountType; }
    public Integer getBalance() { return balance; }
}
