package com.example.authdemo.repository;

import com.example.authdemo.entity.UserProfile;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

  Optional<UserProfile> findByUserUsername(String username);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT p FROM UserProfile p WHERE p.user.username = :username")
  Optional<UserProfile> findByUserUsernameWithLock(@Param("username") String username);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT p FROM UserProfile p WHERE p.user.id = :userId")
  Optional<UserProfile> findByUserIdWithLock(@Param("userId") Long userId);
}
