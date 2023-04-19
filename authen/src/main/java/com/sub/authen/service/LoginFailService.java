package com.sub.authen.service;

public interface LoginFailService {
  void checkLock(String email, String userId, Boolean isLockPermanent);
  Boolean isTemporaryLock(String email);
  void increaseFailAttempts(String email);
  void setLock(String email);
  void resetFailAttempts (String email);
}
