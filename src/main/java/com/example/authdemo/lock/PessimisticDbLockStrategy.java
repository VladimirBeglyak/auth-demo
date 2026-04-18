package com.example.authdemo.lock;

import com.example.authdemo.entity.UserProfile;
import com.example.authdemo.repository.UserProfileRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public class PessimisticDbLockStrategy implements LockStrategy {

  private final UserProfileRepository profileRepository;

  @Override
  @Transactional
  public boolean acquire(String lockKey, long timeoutMs) {
    String username = lockKey.replace("user:", "");
    log.info("Acquiring DB pessimistic lock for user: {}", username);

    Optional<UserProfile> profile = profileRepository.findByUserUsernameWithLock(username);
    return profile.isPresent();
  }

  @Override
  public void release(String lockKey) {
    log.info("DB pessimistic lock will be released automatically at transaction end");
  }
}
