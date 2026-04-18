package com.example.authdemo.controller;

import com.example.authdemo.model.AuthResponse;
import com.example.authdemo.model.LoginRequest;
import com.example.authdemo.model.RefreshTokenRequest;
import com.example.authdemo.model.RegisterRequest;
import com.example.authdemo.service.AuthServiceApi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthServiceApi authServiceApi;

  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
    return authServiceApi.register(request);
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
    return authServiceApi.login(request);
  }

  @PostMapping("/refresh")
  public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
    return authServiceApi.refresh(request);
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(jakarta.servlet.http.HttpServletRequest request) {
    String authHeader = request.getHeader("Authorization");
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      authServiceApi.logout(authHeader);
    }
    return ResponseEntity.ok().build();
  }
}