package com.example.authdemo.controller;

import com.example.authdemo.entity.UserProfile;
import com.example.authdemo.service.UserProfileService;
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
  public ResponseEntity<UserProfile> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
    return ResponseEntity.ok(profileService.getProfile(userDetails.getUsername()));
  }

  @PutMapping
  public ResponseEntity<UserProfile> updateProfile(
      @AuthenticationPrincipal UserDetails userDetails,
      @RequestBody UserProfile profile
  ) {
    return ResponseEntity.ok(profileService.updateProfile(userDetails.getUsername(), profile));
  }
}
