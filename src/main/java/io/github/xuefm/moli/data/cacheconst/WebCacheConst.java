package io.github.xuefm.moli.data.cacheconst;

import java.util.concurrent.TimeUnit;

public interface WebCacheConst {

    String WEB_PREFIX = "web:";

    /**
     * 默认过期时间
     */
    interface DefaultExpired {
        long TIMEOUT  = 1L;
        TimeUnit UNIT = TimeUnit.DAYS;

    }
}
