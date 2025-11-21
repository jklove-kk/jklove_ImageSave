package com.liujie.pictureBackend.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;

@Service
public class RedisService<V> {

    @Resource
    private RedisTemplate<String, V> redisTemplate;

    /**
     * 设置对象值
     */
    public void setObject(String key, V value, Duration timeout) {
        redisTemplate.opsForValue().set(key, value, timeout);
    }

    /**
     * 获取对象值
     */
    public V getObject(String key) {
        return redisTemplate.opsForValue().get(key);
    }


    /**
     * 删除键
     */
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 设置过期时间
     */
    public Boolean expire(String key, Duration timeout) {
        return redisTemplate.expire(key, timeout);
    }

    /**
     * 检查键是否存在
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }
}