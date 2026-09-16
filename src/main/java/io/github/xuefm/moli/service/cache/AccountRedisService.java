package io.github.xuefm.moli.service.cache;

import io.github.xuefm.moli.data.account.LoginUser;
import io.github.xuefm.moli.data.cacheconst.AccountCacheConst;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class AccountRedisService {

    private final RedisTemplate redisTemplate;

    /**
     * set方法（默认一天过期）
     *
     * @param accountId
     * @param value
     */
    public void setAccount(String accountId, LoginUser value) {
        redisTemplate.opsForValue().set(AccountCacheConst.ACCOUNT_PREFIX + accountId, value, AccountCacheConst.DefaultExpired.TIMEOUT, AccountCacheConst.DefaultExpired.UNIT);
    }

    /**
     * set方法（同时设置过期时间）
     *
     * @param accountId
     * @param value
     */
    public void setAccount(String accountId, LoginUser value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(AccountCacheConst.ACCOUNT_PREFIX + accountId, value, timeout, unit);
    }

    /**
     * setIfAbsent方法（默认一天过期）
     *
     * @param accountId
     * @param value
     */
    public boolean setAccountIfAbsent(String accountId, LoginUser value) {
        return redisTemplate.opsForValue().setIfAbsent(AccountCacheConst.ACCOUNT_PREFIX + accountId, value, AccountCacheConst.DefaultExpired.TIMEOUT, AccountCacheConst.DefaultExpired.UNIT);
    }

    /**
     * setIfAbsent方法（同时设置过期时间）
     *
     * @param accountId
     * @param value
     */
    public void setAccountIfAbsent(String accountId, LoginUser value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().setIfAbsent(AccountCacheConst.ACCOUNT_PREFIX + accountId, value, timeout, unit);
    }

    /**
     * get方法
     *
     * @param accountId
     * @return
     */
    public LoginUser getAccount(String accountId) {
        if (redisTemplate.hasKey(AccountCacheConst.ACCOUNT_PREFIX + accountId)) {
            return (LoginUser) redisTemplate.opsForValue().get(AccountCacheConst.ACCOUNT_PREFIX + accountId);
        }
        return null;
    }

    /**
     * del方法
     *
     * @param accountId
     * @return
     */
    public boolean delAccount(String accountId) {
        return redisTemplate.delete(AccountCacheConst.ACCOUNT_PREFIX + accountId);
    }
}
