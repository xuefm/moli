package io.github.xuefm.moli.aop;

import io.github.xuefm.moli.aop.annotation.RepeatSubmit;
import io.github.xuefm.moli.expection.RepeatSubmitException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Component
public class RepeatSubmitAspect {

    private static final String KEY_PREFIX = "repeat_submit:";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private final ExpressionParser parser = new SpelExpressionParser();
    private final ParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    @Around("@annotation(repeatSubmit)")
    public Object around(ProceedingJoinPoint joinPoint, RepeatSubmit repeatSubmit) throws Throwable {
        HttpServletRequest request = getRequest();

        String key = buildKey(joinPoint, request, repeatSubmit);

        Boolean success = stringRedisTemplate.opsForValue()
                .setIfAbsent(key, "1", repeatSubmit.interval(), TimeUnit.MILLISECONDS);

        if (Boolean.FALSE.equals(success)) {
            log.warn("重复提交拦截, key={}", key);
            throw new RepeatSubmitException(repeatSubmit.message());
        }

        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            stringRedisTemplate.delete(key);
            throw e;
        }
    }

    private String buildKey(ProceedingJoinPoint joinPoint, HttpServletRequest request, RepeatSubmit repeatSubmit) {
        String userFlag = getUserFlag(request);
        String path = request.getRequestURI();

        // 1. 优先使用 SpEL
        if (StringUtils.isNotBlank(repeatSubmit.keySpel())) {
            String spelValue = evalSpel(joinPoint, repeatSubmit.keySpel());
            return KEY_PREFIX + userFlag + ":" + path + ":spel:" + md5(spelValue);
        }

        // 2. 指定了 keys，按参数名提取
        if (repeatSubmit.keys().length > 0) {
            String keyPart = buildKeyFromNames(joinPoint, repeatSubmit.keys());
            return KEY_PREFIX + userFlag + ":" + path + ":keys:" + md5(keyPart);
        }

        // 3. 默认：全参数摘要
        String argsHash = hashArgs(joinPoint);
        return KEY_PREFIX + userFlag + ":" + path + ":all:" + argsHash;
    }

    /**
     * 按参数名提取值拼接
     */
    private String buildKeyFromNames(ProceedingJoinPoint joinPoint, String[] keys) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();

        // 形参名 -> 值
        String[] paramNames = nameDiscoverer.getParameterNames(method);
        Map<String, Object> nameValueMap = new LinkedHashMap<>();
        if (paramNames != null) {
            for (int i = 0; i < paramNames.length; i++) {
                nameValueMap.put(paramNames[i], args[i]);
            }
        }

        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            Object value = nameValueMap.get(key);
            sb.append(key).append("=").append(value).append("&");
        }
        return sb.toString();
    }

    /**
     * SpEL 求值
     */
    private String evalSpel(ProceedingJoinPoint joinPoint, String spel) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();
        String[] paramNames = nameDiscoverer.getParameterNames(method);

        EvaluationContext context = new StandardEvaluationContext();
        if (paramNames != null) {
            for (int i = 0; i < paramNames.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
        }
        Expression expression = parser.parseExpression(spel);
        Object value = expression.getValue(context);
        return value == null ? "" : value.toString();
    }

    private String hashArgs(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = nameDiscoverer.getParameterNames(signature.getMethod());

        StringBuilder sb = new StringBuilder();
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                if (arg == null
                        || arg instanceof MultipartFile
                        || arg instanceof HttpServletRequest
                        || arg instanceof jakarta.servlet.http.HttpServletResponse) {
                    continue;
                }
                String name = (paramNames != null && i < paramNames.length) ? paramNames[i] : ("arg" + i);
                sb.append(name).append("=").append(arg).append("&");
            }
        }
        return md5(sb.toString());
    }

    private String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return String.valueOf(Objects.hashCode(input));
        }
    }

    private String getUserFlag(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (StringUtils.isNotBlank(token)) {
            return token;
        }
        return getIp(request);
    }

    private String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private HttpServletRequest getRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attrs != null ? attrs.getRequest() : null;
    }
}
