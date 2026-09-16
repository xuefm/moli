package io.github.xuefm.moli.service.cache;

import io.github.xuefm.moli.data.cacheconst.CacheConst;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class RedisCommonService<V> {

    private final RedisTemplate redisTemplate;

    /**
     * set方法（默认一天过期）
     *
     * @param key
     * @param value
     */
    public void set(String key, V value) {
        redisTemplate.opsForValue().set(key, value, CacheConst.DefaultExpired.TIMEOUT, CacheConst.DefaultExpired.UNIT);
    }

    /**
     * set方法（同时设置过期时间）
     *
     * @param key
     * @param value
     */
    public void set(String key, V value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * setIfAbsent方法（默认一天过期）
     *
     * @param key
     * @param value
     */
    public boolean setIfAbsent(String key, V value) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, CacheConst.DefaultExpired.TIMEOUT, CacheConst.DefaultExpired.UNIT);
    }

    /**
     * setIfAbsent方法（同时设置过期时间）
     *
     * @param key
     * @param value
     */
    public void setIfAbsent(String key, V value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().setIfAbsent(key, value, timeout, unit);
    }

    public V get(String key) {
        return (V) redisTemplate.opsForValue().get(key);
    }


}
