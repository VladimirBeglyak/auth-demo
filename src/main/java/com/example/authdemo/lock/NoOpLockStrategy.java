package com.example.authdemo.lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NoOpLockStrategy implements LockStrategy {

  @Override
  public boolean acquire(String lockKey, long timeoutMs) {
    log.debug("No-op lock: skipping lock for key {}", lockKey);
    return true;
  }

  @Override
  public void release(String lockKey) {
    log.debug("No-op lock: nothing to release for key {}", lockKey);
  }
}
