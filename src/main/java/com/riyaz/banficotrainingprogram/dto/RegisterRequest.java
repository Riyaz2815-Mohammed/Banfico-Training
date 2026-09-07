package com.riyaz.banficotrainingprogram.dto;

public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String pan;
    private String phoneNumber;
    private String username;
    private String temporaryPassword;

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPan() { return pan; }
    public void setPan(String pan) { this.pan = pan; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getTemporaryPassword() { return temporaryPassword; }
    public void setTemporaryPassword(String temporaryPassword) { this.temporaryPassword = temporaryPassword; }
}
