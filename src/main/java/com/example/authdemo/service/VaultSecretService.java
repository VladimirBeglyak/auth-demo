package com.example.authdemo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class VaultSecretService {

  @Value("${vault.addr:http://localhost:8200}")
  private String vaultAddr;

  @Value("${vault.token:root-token-2026}")
  private String vaultToken;

  private final RestTemplate restTemplate = new RestTemplate();
  private final ObjectMapper objectMapper = new ObjectMapper();

  public String readSecret(String path, String key) {
    try {
      String url = vaultAddr + "/v1/" + path;
      HttpHeaders headers = new HttpHeaders();
      headers.set("X-Vault-Token", vaultToken);
      HttpEntity<?> entity = new HttpEntity<>(headers);
      ResponseEntity<String> response = restTemplate.exchange(
          url, HttpMethod.GET, entity, String.class
      );
      JsonNode root = objectMapper.readTree(response.getBody());
      JsonNode dataNode = root.path("data").path("data");
      if (dataNode.isMissingNode()) {
        dataNode = root.path("data");
      }
      return dataNode.path(key).asText();
    } catch (Exception e) {
      log.error("Failed to read secret from Vault: {}", e.getMessage());
      throw new RuntimeException("Could not read secret from Vault", e);
    }
  }
}