package com.sub.authen.service.impl;


import com.example.BadRequestException;
import com.sub.authen.facade.FacadeService;
import com.sub.authen.service.LoginFailService;
import com.sub.authen.utils.DateUtils;
import com.thoughtworks.xstream.core.BaseException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import static com.sub.authen.constant.CacheConstant.LoginFail.KEY_CACHE_FAIL_ATTEMPTS;
import static com.sub.authen.constant.CacheConstant.LoginFail.KEY_CACHE_UNLOCK_TIME;
import static com.sub.authen.constant.LoginFailConstant.*;

@Service
public class LoginFailServiceImpl extends BaseRedisHashServiceImpl<Long> implements
        LoginFailService {

  public LoginFailServiceImpl(
      RedisTemplate<String, Object> redisTemplate, FacadeService facadeService) {
    super(redisTemplate);
    this.facadeService = facadeService;
  }
  private final FacadeService facadeService;
  @Override
  public void checkLock(String email, String userId, Boolean isLockPermanent) {

    if (isLockPermanent) {
      throw new BadRequestException("Account was locked");
    }
    if (isTemporaryLock(email)) {
      throw new BadRequestException( "Account is locking temporarily");
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
      facadeService.enableLockPermanent(email);
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
