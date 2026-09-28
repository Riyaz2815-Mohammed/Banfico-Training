package com.riyaz.banficotrainingprogram.customer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CustomerRequest {
    private String pan;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    public CustomerRequest() {}

    @NotBlank(message = "Pan is must")
    @Size(min = 10, max = 10)
    public String getPan() { return pan; }
    public void setPan(String pan) { this.pan = pan; }

    @NotBlank(message = "FirstName is missing")
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    @NotBlank(message = "Missing LastName")
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    @NotBlank(message = "Email is Must")
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Size(min = 10, max = 10)
    @NotBlank(message = "Need phoneNumber")
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}
