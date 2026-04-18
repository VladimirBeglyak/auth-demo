package com.example.authdemo.model;

import com.example.authdemo.entity.UserProfile;

public record UserProfileResponse(
    String username,
    String fullName,
    String email,
    String phoneNumber,
    String address
) {

  public static UserProfileResponse fromEntity(UserProfile profile) {
    return new UserProfileResponse(
        profile.getUser().getUsername(),
        profile.getFullName(),
        profile.getEmail(),
        profile.getPhoneNumber(),
        profile.getAddress()
    );
  }
}
