package com.example.authdemo.config;

import com.example.authdemo.lock.LockStrategy;
import com.example.authdemo.lock.NoOpLockStrategy;
import com.example.authdemo.lock.PessimisticDbLockStrategy;
import com.example.authdemo.lock.PostgresAdvisoryLockStrategy;
import com.example.authdemo.lock.RedisLockStrategy;
import com.example.authdemo.repository.UserProfileRepository;
import jakarta.persistence.EntityManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class LockConfig {

  @Bean
  @ConditionalOnProperty(name = "app.lock.type", havingValue = "redis")
  public LockStrategy redisLockStrategy(StringRedisTemplate redisTemplate) {
    return new RedisLockStrategy(redisTemplate);
  }

  @Bean
  @ConditionalOnProperty(name = "app.lock.type", havingValue = "advisory", matchIfMissing = true)
  public LockStrategy postgresAdvisoryLockStrategy(EntityManager entityManager) {
    return new PostgresAdvisoryLockStrategy(entityManager);
  }

  @Bean
  @ConditionalOnProperty(name = "app.lock.type", havingValue = "pessimistic_db")
  public LockStrategy pessimisticDbLockStrategy(UserProfileRepository profileRepository) {
    return new PessimisticDbLockStrategy(profileRepository);
  }

  @Bean
  @ConditionalOnProperty(name = "app.lock.type", havingValue = "none")
  public LockStrategy noOpLockStrategy() {
    return new NoOpLockStrategy();
  }
}
