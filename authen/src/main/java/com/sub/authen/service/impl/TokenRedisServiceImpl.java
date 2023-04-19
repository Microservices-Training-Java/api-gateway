package com.sub.authen.service.impl;

import com.sub.authen.service.TokenRedisService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class TokenRedisServiceImpl extends BaseRedisHashServiceImpl<String> implements
        TokenRedisService {

  public TokenRedisServiceImpl(
      RedisTemplate<String, Object> redisTemplate) {
    super(redisTemplate);
  }
}
