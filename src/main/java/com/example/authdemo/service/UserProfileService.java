package com.example.authdemo.service;

import com.example.authdemo.entity.User;
import com.example.authdemo.entity.UserProfile;
import com.example.authdemo.lock.LockStrategy;
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

  public UserProfile getProfile(String username) {
    return profileRepository.findByUserUsername(username)
        .orElseGet(() -> createDefaultProfile(username));
  }

  @Transactional
  public UserProfile updateProfile(String username, UserProfile updatedData) {
    String lockKey = "user:" + username;

    if (!lockStrategy.acquire(lockKey, 5000)) {
      throw new RuntimeException("Could not acquire lock for profile update. Try again later.");
    }

    try {
      UserProfile profile = profileRepository.findByUserUsername(username)
          .orElseGet(() -> createDefaultProfile(username));

      profile.setFullName(updatedData.getFullName());
      profile.setEmail(updatedData.getEmail());
      profile.setPhoneNumber(updatedData.getPhoneNumber());
      profile.setAddress(updatedData.getAddress());

      log.info("Updating profile for user: {}", username);
      return profileRepository.save(profile);
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
