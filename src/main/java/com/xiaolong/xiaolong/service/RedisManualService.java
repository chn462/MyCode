package com.xiaolong.xiaolong.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisManualService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 存储字符串
    public void setStringValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }
    // 存储字符串并设置过期时间
    public void setStringValue(String key, String value, int timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }
    // 获取字符串
    public String getStringValue(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }
    // 存储对象（会被序列化为 JSON）
    public void setObject(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }
    // 存储对象并设置过期时间
    public void setObject(String key, Object value, int timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }
    // 获取对象
    public Object getObject(String key) {
        return redisTemplate.opsForValue().get(key);
    }
    // 删除键
    public Boolean deleteKey(String key) {
        return redisTemplate.delete(key);
    }
    // 设置键的过期时间
    public Boolean setKeyExpire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }
}
