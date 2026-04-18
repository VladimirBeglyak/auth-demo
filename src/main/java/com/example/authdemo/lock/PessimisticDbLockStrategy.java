package com.example.authdemo.lock;

import com.example.authdemo.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PessimisticDbLockStrategy implements LockStrategy {

  private final UserProfileRepository profileRepository;

  @Override
  @Transactional
  public boolean acquire(String lockKey, long timeoutMs) {
    String username = lockKey.replace("user:", "");
    log.info("Acquiring DB pessimistic lock for user: {}", username);

    profileRepository.findByUserUsernameWithLock(username);
    return true;
  }

  @Override
  public void release(String lockKey) {
    log.info("DB pessimistic lock will be released automatically at transaction end");
  }
}
