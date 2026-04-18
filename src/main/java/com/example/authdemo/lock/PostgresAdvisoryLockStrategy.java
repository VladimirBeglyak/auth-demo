package com.example.authdemo.lock;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public class PostgresAdvisoryLockStrategy implements LockStrategy {

  private final EntityManager entityManager;

  @Override
  @Transactional
  public boolean acquire(String lockKey, long timeoutMs) {
    long lockId = lockKey.hashCode();
    Boolean acquired = (Boolean) entityManager.createNativeQuery("SELECT pg_try_advisory_lock(:id)")
        .setParameter("id", lockId)
        .getSingleResult();

    if (acquired) {
      log.info("Postgres advisory lock acquired for key: {} (id: {})", lockKey, lockId);
    } else {
      log.warn("Failed to acquire Postgres advisory lock for key: {}", lockKey);
    }
    return acquired;
  }

  @Override
  @Transactional
  public void release(String lockKey) {
    long lockId = lockKey.hashCode();
    entityManager.createNativeQuery("SELECT pg_advisory_unlock(:id)")
        .setParameter("id", lockId)
        .executeUpdate();
    log.info("Postgres advisory lock released for key: {}", lockKey);
  }
}
