package com.riyaz.banficotrainingprogram.customer.dto;

public class CreateManagerResponse {
    private String keycloakId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String message;

    public CreateManagerResponse(String keycloakId, String username, String email, String firstName, String lastName) {
        this.keycloakId = keycloakId;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.message = "Manager account created successfully";
    }

    public String getKeycloakId() { return keycloakId; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getMessage() { return message; }
}
