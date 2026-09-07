package com.riyaz.banficotrainingprogram.dto;

import java.util.UUID;

public class RegisterResponse {
    private UUID customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String pan;
    private String phoneNumber;
    private String keycloakUserId;
    private String message;

    public RegisterResponse(UUID customerId, String firstName, String lastName, String email,
                            String pan, String phoneNumber, String keycloakUserId) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.pan = pan;
        this.phoneNumber = phoneNumber;
        this.keycloakUserId = keycloakUserId;
        this.message = "User registered successfully. They can now log in with the temporary password.";
    }

    public UUID getCustomerId() { return customerId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPan() { return pan; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getKeycloakUserId() { return keycloakUserId; }
    public String getMessage() { return message; }
}
