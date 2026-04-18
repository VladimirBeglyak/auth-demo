package com.example.authdemo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {

  private final VaultSecretService vaultSecretService;

  @Value("${jwt.expiration}")
  private Long accessExpiration;

  @Value("${jwt.refresh-expiration}")
  private Long refreshExpiration;

  private String secret;

  @PostConstruct
  public void init() {
    this.secret = vaultSecretService.readSecret("secret/data/jwt", "secret");
    if (this.secret == null || this.secret.isBlank()) {
      throw new IllegalStateException("JWT secret not found in Vault");
    }
  }

  private SecretKey getSigningKey() {
    byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  private Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  public String generateAccessToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("type", "access");
    claims.put("authorities", userDetails.getAuthorities());
    return createToken(claims, userDetails.getUsername(), accessExpiration);
  }

  public String generateRefreshToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("type", "refresh");
    return createToken(claims, userDetails.getUsername(), refreshExpiration);
  }

  private String createToken(Map<String, Object> claims, String subject, Long expiration) {
    return Jwts.builder()
        .claims(claims)
        .subject(subject)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(getSigningKey())
        .compact();
  }

  public Boolean validateAccessToken(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    String claim = extractClaim(token, claims -> claims.get("type", String.class));
    return (
        username.equals(userDetails.getUsername())
            && !isTokenExpired(token)
            && "access".equals(claim)
    );
  }

  public Boolean validateRefreshToken(String token) {
    try {
      String claim = extractClaim(token, claims -> claims.get("type", String.class));
      return !isTokenExpired(token) && "refresh".equals(claim);
    } catch (Exception e) {
      return false;
    }
  }
}