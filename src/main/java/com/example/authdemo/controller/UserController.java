package com.example.authdemo.controller;

import com.example.authdemo.model.UserInfoResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

  @GetMapping("/me")
  public UserInfoResponse getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
    String role = userDetails.getAuthorities().stream()
        .findFirst()
        .map(GrantedAuthority::getAuthority)
        .orElse("UNKNOWN");
    return new UserInfoResponse(userDetails.getUsername(), role);
  }
}