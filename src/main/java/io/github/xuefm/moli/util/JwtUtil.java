package io.github.xuefm.moli.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.jwt.JWT;
import io.github.xuefm.moli.data.web.TokenData;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.Date;

@Slf4j
@UtilityClass
public class JwtUtil {
    // 密钥
    byte[] key = "1234567890".getBytes();

    public static String createToken(TokenData tokenData) {
        JWT jwt = JWT.create()
                .setKey(key)
                .setExpiresAt(generateExpireDate());
        BeanUtil.beanToMap(tokenData).forEach(jwt::setPayload);
        String sign = jwt.sign();
        return sign;
    }

    /**
     * 解析
     *
     * @param token
     * @param name
     * @return
     */
    public static String resolve(String token, String name) {
        JWT jwt = JWT.of(token);
        if (jwt.setKey(key).verify())
            return (String) jwt.getPayload(name);
        else
            return null;
    }

    /**
     * 解析
     *
     * @param token
     * @return
     */
    public static TokenData resolveTobTokenData(String token) {
        JWT jwt = JWT.of(token);
        return BeanUtil.mapToBean(jwt.getPayloads(), TokenData.class, true);
    }

    /**
     * 验证
     *
     * @param token
     * @return
     */
    public static boolean verification(String token) {
        // 默认验证HS265的算法
        boolean verify = JWT.of(token).setKey(key).verify();
        return verify;
    }

    /**
     * 指定过期时间
     */
    private static Date generateExpireDate() {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, 3);
        return instance.getTime();
    }
}
