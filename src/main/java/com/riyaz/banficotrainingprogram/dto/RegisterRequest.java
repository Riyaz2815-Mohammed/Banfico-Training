package com.riyaz.banficotrainingprogram.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String pan;
    private String phoneNumber;
    private String username;
    private String temporaryPassword;

    @NotBlank(message = "First name is required")
    @Size(max = 20, message = "First name must be at most 20 characters")
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    @NotBlank(message = "Last name is required")
    @Size(max = 20, message = "Last name must be at most 20 characters")
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 50, message = "Email must be at most 50 characters")
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @NotBlank(message = "PAN is required")
    @Size(min = 10, max = 10, message = "PAN must be exactly 10 characters")
    public String getPan() { return pan; }
    public void setPan(String pan) { this.pan = pan != null ? pan.toUpperCase() : null; }

    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 20, message = "Phone number must be 10-20 digits")
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    @NotBlank(message = "Username is required")
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    @NotBlank(message = "Temporary password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    public String getTemporaryPassword() { return temporaryPassword; }
    public void setTemporaryPassword(String temporaryPassword) { this.temporaryPassword = temporaryPassword; }
}
