package com.example.authdemo.model;

public record AuthResponse(String accessToken, String refreshToken, String type) {

  public AuthResponse(
      String accessToken,
      String refreshToken
  ) {
    this(accessToken, refreshToken, "Bearer");
  }
}