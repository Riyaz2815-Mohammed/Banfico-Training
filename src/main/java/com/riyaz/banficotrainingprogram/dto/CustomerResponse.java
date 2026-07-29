package com.riyaz.banficotrainingprogram.dto;

import java.util.UUID;

public class CustomerResponse {
    private final UUID id;
    private final String pan;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phoneNumber;

    public CustomerResponse(
            UUID id,
            String pan,
            String firstName,
            String lastName,
            String email,
            String phoneNumber) {

        this.id = id;
        this.pan = pan;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public UUID getId() {
        return id;
    }

    public String getPan() {
        return pan;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

}
