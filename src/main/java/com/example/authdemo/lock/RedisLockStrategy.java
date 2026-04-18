package com.example.authdemo.lock;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisLockStrategy implements LockStrategy {

  private final StringRedisTemplate redisTemplate;
  private static final String LOCK_PREFIX = "lock:profile:";

  @Override
  public boolean acquire(String lockKey, long timeoutMs) {
    String key = LOCK_PREFIX + lockKey;
    Boolean success = redisTemplate.opsForValue().setIfAbsent(key, "locked", Duration.ofMillis(timeoutMs));
    boolean acquired = Boolean.TRUE.equals(success);
    if (acquired) {
      log.info("Redis lock acquired for key: {}", key);
    } else {
      log.warn("Failed to acquire Redis lock for key: {}", key);
    }
    return acquired;
  }

  @Override
  public void release(String lockKey) {
    String key = LOCK_PREFIX + lockKey;
    redisTemplate.delete(key);
    log.info("Redis lock released for key: {}", key);
  }
}
