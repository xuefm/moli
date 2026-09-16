package io.github.xuefm.moli.aop;


import cn.hutool.json.JSONUtil;
import io.github.xuefm.moli.data.web.Results;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
@Order(2)
public class WebLogAspect {

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
        System.out.println("----------- 前置通知打印所有参数 -----------");
        String s = JSONUtil.toJsonPrettyStr(joinPoint);
        System.out.println(s);
        System.out.println("----------- 前置通知 -----------");
        Signature signature = joinPoint.getSignature();
        System.out.println("返回目标方法的签名：" + signature);
        System.out.println("代理的是哪一个方法：" + signature.getName());
        Object[] args = joinPoint.getArgs();
        System.out.println("获取目标方法的参数信息：" + Arrays.asList(args));

    }

    /**
     * 最终通知，目标方法执行完之后执行
     */
    @After("pointCut()")
    public void afterAdvice(){
        System.out.println("----------- 最终通知 -----------");

    }

    /**
     * 后置返回通知
     * 如果参数中的第一个参数为JoinPoint，则第二个参数为返回值的信息
     * 如果参数中的第一个参数不为JoinPoint，则第一个参数为returning中对应的参数
     * returning 只有目标方法返回值与通知方法相应参数类型时才能执行后置返回通知，否则不执行
     * @param joinPoint
     * @param keys 参数类型必须和切入的方法返回值类型一致
     */
    @AfterReturning(value = "pointCut()", returning = "keys")
    public void afterReturningAdvice(JoinPoint joinPoint, Results keys){
        System.out.println("----------- 后置返回通知 -----------");
        Signature signature = joinPoint.getSignature();
        System.out.println(signature.getName());
        Object[] args = joinPoint.getArgs();
        System.out.println(Arrays.toString(args));
        String kind = joinPoint.getKind();
        System.out.println(kind);
        System.out.println("后置返回通知的返回值：" + keys);
        System.out.println("----------- 后置返回通知结束 -----------");
    }

    /**
     * 后置异常通知
     * 定义一个名字，该名字用于匹配通知实现方法的一个参数名，当目标方法抛出异常返回后，将把目标方法抛出的异常传给通知方法；
     * throwing 只有目标方法抛出的异常与通知方法相应参数异常类型时才能执行后置异常通知，否则不执行，
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(value = "pointCut()", throwing = "e")
    public void afterThrowingAdvice(JoinPoint joinPoint, NullPointerException e){
        System.out.println("----------- 后置异常通知 -----------");
    }

    /**
     * 环绕通知
     * 环绕通知非常强大，可以决定目标方法是否执行，什么时候执行，执行时是否需要替换方法参数，执行完毕是否需要替换返回值。
     * 环绕通知第一个参数必须是org.aspectj.lang.ProceedingJoinPoint类型
     * @param proceedingJoinPoint
     */
    @Around("pointCut()")
    public Object aroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("----------- 环绕通知 -----------");
        System.out.println("环绕通知的目标方法名：" + proceedingJoinPoint.getSignature().getName());
        try {
            Object proceed = proceedingJoinPoint.proceed();
            return proceed;
        } catch (Throwable throwable) {
            log.info("异常交由全局异常处理");
            throw throwable;
        }finally {
            System.out.println("---------- 环绕通知结束 -------------");
        }
    }

}


