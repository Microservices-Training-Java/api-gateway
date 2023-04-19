package com.sub.authen.service.impl;

import java.util.Map;

import com.sub.authen.service.BaseRedisHashService;
import com.sub.authen.utils.MapperUtil;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

public class BaseRedisHashServiceImpl<T> implements BaseRedisHashService<T> {

  private final HashOperations<String, String, Object> hashOperations;

  public BaseRedisHashServiceImpl(RedisTemplate<String, Object> redisTemplate) {
    this.hashOperations = redisTemplate.opsForHash();
  }

  @Override
  public void delete(String key) {
    hashOperations.delete(key);
  }

  public void delete(String key, String field) {
    hashOperations.delete(key, field);
  }

  @Override
  public Map<String, Object> get(String key) {
    return hashOperations.entries(key);
  }

  @Override
  public Object get(String key, String field) {
    return hashOperations.get(key, field);
  }

  @Override
  public void set(String key, T object) {
    var data = MapperUtil.toMap(object);
    hashOperations.putAll(key, data);
  }

  @Override
  public void set(String key, String field, Object value) {
    hashOperations.put(key, field, value);
  }
}
