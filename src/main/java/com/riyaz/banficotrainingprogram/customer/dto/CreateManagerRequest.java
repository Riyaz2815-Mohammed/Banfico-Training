package com.riyaz.banficotrainingprogram.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateManagerRequest {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String temporaryPassword;

    @NotBlank(message = "Username is required")
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @NotBlank(message = "First name is required")
    @Size(max = 20)
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    @NotBlank(message = "Last name is required")
    @Size(max = 20)
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    @NotBlank(message = "Temporary password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    public String getTemporaryPassword() { return temporaryPassword; }
    public void setTemporaryPassword(String temporaryPassword) { this.temporaryPassword = temporaryPassword; }
}
