package com.example.authdemo.service;

import com.example.authdemo.entity.User;
import com.example.authdemo.entity.UserProfile;
import com.example.authdemo.lock.LockStrategy;
import com.example.authdemo.model.UserProfileResponse;
import com.example.authdemo.model.UserProfileUpdateRequest;
import com.example.authdemo.repository.UserProfileRepository;
import com.example.authdemo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProfileService {

  private final UserProfileRepository profileRepository;
  private final UserRepository userRepository;
  private final LockStrategy lockStrategy;

  public UserProfileResponse getProfile(String username) {
    UserProfile profile = profileRepository.findByUserUsername(username)
        .orElseGet(() -> createDefaultProfile(username));
    return UserProfileResponse.fromEntity(profile);
  }

  @Transactional
  public UserProfileResponse updateProfile(String username, UserProfileUpdateRequest updatedData) {
    String lockKey = "user:" + username;

    if (!lockStrategy.acquire(lockKey, 5000)) {
      throw new RuntimeException("Could not acquire lock for profile update. Try again later.");
    }

    try {
      UserProfile profile = profileRepository.findByUserUsername(username)
          .orElseGet(() -> createDefaultProfile(username));

      profile.setFullName(updatedData.fullName());
      profile.setEmail(updatedData.email());
      profile.setPhoneNumber(updatedData.phoneNumber());
      profile.setAddress(updatedData.address());

      log.info("Updating profile for user: {}", username);
      UserProfile saved = profileRepository.save(profile);
      return UserProfileResponse.fromEntity(saved);
    } catch (OptimisticLockingFailureException e) {
      log.error("Optimistic lock failure for user {}: {}", username, e.getMessage());
      throw new RuntimeException("The profile was updated by another user. Please refresh and try again.");
    } finally {
      lockStrategy.release(lockKey);
    }
  }

  private UserProfile createDefaultProfile(String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));

    UserProfile profile = UserProfile.builder()
        .user(user)
        .build();
    return profileRepository.save(profile);
  }
}
