package com.example.authdemo.service;

import com.example.authdemo.model.AuthResponse;
import com.example.authdemo.model.LoginRequest;
import com.example.authdemo.model.RefreshTokenRequest;
import com.example.authdemo.model.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceApi {

  private final AuthenticationManager authenticationManager;
  private final UserService userService;
  private final JwtService jwtService;
  private final TokenBlacklistService blacklistService;

  public void logout(String token) {
    String jwt = token.substring(7);
    long expirationTime = jwtService.extractExpiration(jwt).getTime();
    long currentTime = System.currentTimeMillis();
    long remainingTime = expirationTime - currentTime;

    if (remainingTime > 0) {
      blacklistService.blacklistToken(jwt, remainingTime);
    }
  }

  public ResponseEntity<AuthResponse> login(LoginRequest request) {
    authenticate(request.username(), request.password());
    return getAuthResponse(request.username());
  }

  public ResponseEntity<AuthResponse> refresh(RefreshTokenRequest request) {
    String refreshToken = request.refreshToken();
    if (!jwtService.validateRefreshToken(refreshToken)) {
      throw new RuntimeException("Invalid or expired refresh token");
    }
    String username = jwtService.extractUsername(refreshToken);
    return getAuthResponse(username);
  }

  public ResponseEntity<AuthResponse> register(RegisterRequest request) {
    userService.register(request);
    authenticate(request.username(), request.password());
    return getAuthResponse(request.username());
  }

  private ResponseEntity<AuthResponse> getAuthResponse(String request) {
    UserDetails userDetails = userService.loadUserByUsername(request);
    String accessToken = jwtService.generateAccessToken(userDetails);
    String refreshToken = jwtService.generateRefreshToken(userDetails);
    return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken));
  }

  private void authenticate(String username, String password) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
  }
}
