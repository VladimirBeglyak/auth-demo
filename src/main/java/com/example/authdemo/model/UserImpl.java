package com.example.authdemo.model;

import com.example.authdemo.entity.User;
import com.example.authdemo.entity.dictionary.Role;
import jakarta.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public record UserImpl(
    String username,
    String password,
    Role role
) implements UserDetails {

  public UserImpl(@NotNull User user) {
    this(
        user.getUsername(),
        user.getPassword(),
        user.getRole()
    );
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.name()));
  }

  @Override
  public @Nullable String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
