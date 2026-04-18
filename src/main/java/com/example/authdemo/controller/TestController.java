package com.example.authdemo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

  @GetMapping("/hello")
  public String hello(@AuthenticationPrincipal UserDetails user) {
    return "Hello, " + user.getUsername() + "! You have accessed a secured endpoint.";
  }

  @GetMapping("/admin")
  public String adminOnly() {
    return "Admin area – only users with ROLE_ADMIN can see this.";
  }
}
