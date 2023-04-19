package com.sub.authen.service.impl;


import com.sub.authen.exception.BaseException;
import com.sub.authen.service.AuthAccountService;
import com.sub.authen.service.LoginFailService;
import com.sub.authen.utils.DateUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import static com.sub.authen.constant.CacheConstant.LoginFail.KEY_CACHE_FAIL_ATTEMPTS;
import static com.sub.authen.constant.CacheConstant.LoginFail.KEY_CACHE_UNLOCK_TIME;
import static com.sub.authen.constant.LoginFailConstant.*;

@Service
public class LoginFailServiceImpl extends BaseRedisHashServiceImpl<Long> implements
        LoginFailService {

  public LoginFailServiceImpl(
      RedisTemplate<String, Object> redisTemplate, AuthAccountService authAccountService) {
    super(redisTemplate);
    this.authAccountService = authAccountService;
  }
  private final AuthAccountService authAccountService;
  @Override
  public void checkLock(String email, String userId, Boolean isLockPermanent) {

    if (isLockPermanent) {
      throw new BaseException(300, "300", "Account was locked");
    }
    if (isTemporaryLock(email)) {
      throw new BaseException(301,"301", "Account is locking temporarily");
    }
  }
  @Override
  public Boolean isTemporaryLock(String email) {
    Long unlockTime = (Long) get(KEY_CACHE_UNLOCK_TIME, email);
    if (unlockTime == null) {
      return false;
    }
    return DateUtils.getCurrentEpoch() < unlockTime;
  }
  @Override
  public void increaseFailAttempts(String email) {
    Long failAttempts = (Long) get(KEY_CACHE_FAIL_ATTEMPTS, email);
    if (failAttempts == null) {
      failAttempts = INIT_FAIL_ATTEMPTS;
    }
    failAttempts++;
    set(KEY_CACHE_FAIL_ATTEMPTS, email, failAttempts);
  }
  @Override
  public void setLock(String email) {
    Long failAttempts = (Long) get(KEY_CACHE_FAIL_ATTEMPTS, email);
    if (failAttempts.equals(THIRD_LOCK_LIMIT)) {
      authAccountService.enableLockPermanent(email);
    }
    if (failAttempts.equals(SECOND_LOCK_LIMIT)) {
      set(KEY_CACHE_UNLOCK_TIME, email, DateUtils.getCurrentEpoch() + SECOND_LOCK_TIME);
    }
    if (failAttempts.equals(FIRST_LOCK_LIMIT)) {
      set(KEY_CACHE_UNLOCK_TIME, email, DateUtils.getCurrentEpoch() + FIRST_LOCK_TIME);
    }
  }
  @Override
  public void resetFailAttempts(String email) {
    delete(KEY_CACHE_FAIL_ATTEMPTS, email);
    delete(KEY_CACHE_UNLOCK_TIME, email);
  }
}
