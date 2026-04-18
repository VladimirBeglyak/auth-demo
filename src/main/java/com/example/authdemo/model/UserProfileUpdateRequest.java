package com.example.authdemo.model;

import jakarta.validation.constraints.Email;

public record UserProfileUpdateRequest(
    String fullName,
    @Email(message = "Invalid email format")
    String email,
    @jakarta.validation.constraints.Pattern(
        regexp = "^\\+375\\d{9}$",
        message = "Phone number must be in format +375XXXXXXXXX"
    )
    String phoneNumber,
    String address
) {

}
