package com.example.authdemo.service;

import com.example.authdemo.entity.User;
import com.example.authdemo.entity.dictionary.Role;
import com.example.authdemo.model.RegisterRequest;
import com.example.authdemo.model.UserImpl;
import com.example.authdemo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
    return userRepository.findByUsername(username)
        .map(UserImpl::new)
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
  }

  public User register(RegisterRequest request) {
    if (userRepository.existsByUsername(request.username())) {
      throw new RuntimeException("Username already exists");
    }
    User user = User.builder()
        .username(request.username())
        .password(passwordEncoder.encode(request.password()))
        .role(Role.ROLE_USER)
        .build();
    return userRepository.save(user);
  }
}