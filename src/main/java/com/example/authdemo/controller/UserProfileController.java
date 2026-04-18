package com.example.authdemo.controller;

import com.example.authdemo.model.UserProfileResponse;
import com.example.authdemo.model.UserProfileUpdateRequest;
import com.example.authdemo.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/profile")
@RequiredArgsConstructor
public class UserProfileController {

  private final UserProfileService profileService;

  @GetMapping
  public ResponseEntity<UserProfileResponse> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
    return ResponseEntity.ok(profileService.getProfile(userDetails.getUsername()));
  }

  @PutMapping
  public ResponseEntity<UserProfileResponse> updateProfile(
      @AuthenticationPrincipal UserDetails userDetails,
      @Valid @RequestBody UserProfileUpdateRequest profile
  ) {
    return ResponseEntity.ok(profileService.updateProfile(userDetails.getUsername(), profile));
  }
}
