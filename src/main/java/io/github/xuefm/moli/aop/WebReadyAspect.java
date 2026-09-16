package io.github.xuefm.moli.aop;


import io.github.xuefm.moli.data.system.SystemConfig;
import io.github.xuefm.moli.expection.BusinessException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Slf4j
@Aspect
@Component
@Order(99)
@AllArgsConstructor
public class WebReadyAspect {
    private final SystemConfig systemConfig;

    /**
     * 切入点
     * 匹配io.github.xuefm.moli.controller包及其子包下的所有类的所有方法
     */
    @Pointcut("execution(* io.github.xuefm.moli.controller.*.*(..))")
    public void pointCut(){

    }

    /**
     * 前置通知，目标方法调用前被调用
     */
    @Before("pointCut()")
    public void beforeAdvice(JoinPoint joinPoint){
        if (!systemConfig.getReady()) {
            throw new BusinessException("系统还没有准备好,请稍等...");
        }
    }

}


