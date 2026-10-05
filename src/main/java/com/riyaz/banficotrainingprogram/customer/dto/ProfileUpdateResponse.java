package com.riyaz.banficotrainingprogram.customer.dto;

import java.util.UUID;

public class ProfileUpdateResponse {
    private UUID id;
    private String pan;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private boolean emailChanged;

    public ProfileUpdateResponse(UUID id, String pan, String firstName, String lastName, String email, String phoneNumber, boolean emailChanged) {
        this.id = id;
        this.pan = pan;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.emailChanged = emailChanged;
    }

    public UUID getId() { return id; }
    public String getPan() { return pan; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public boolean isEmailChanged() { return emailChanged; }
}
