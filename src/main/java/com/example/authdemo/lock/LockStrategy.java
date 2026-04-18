package com.example.authdemo.lock;

public interface LockStrategy {

  boolean acquire(String lockKey, long timeoutMs);

  void release(String lockKey);
}
