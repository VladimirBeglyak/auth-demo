package com.example.authdemo.config;

import com.example.authdemo.lock.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LockConfig {

  @Bean
  @ConditionalOnProperty(name = "app.lock.type", havingValue = "redis")
  public LockStrategy redisLockStrategy(RedisLockStrategy redisLock) {
    return redisLock;
  }

  @Bean
  @ConditionalOnProperty(name = "app.lock.type", havingValue = "advisory", matchIfMissing = true)
  public LockStrategy postgresAdvisoryLockStrategy(PostgresAdvisoryLockStrategy postgresLock) {
    return postgresLock;
  }

  @Bean
  @ConditionalOnProperty(name = "app.lock.type", havingValue = "pessimistic_db")
  public LockStrategy pessimisticDbLockStrategy(PessimisticDbLockStrategy dbLock) {
    return dbLock;
  }

  @Bean
  @ConditionalOnProperty(name = "app.lock.type", havingValue = "none")
  public LockStrategy noOpLockStrategy(NoOpLockStrategy noOpLock) {
    return noOpLock;
  }
}
