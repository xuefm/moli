package io.github.xuefm.moli.aop.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepeatSubmit {

    /**
     * 防重间隔时间，单位毫秒，默认 5 秒
     */
    long interval() default 5000;

    /**
     * 提示信息
     */
    String message() default "请勿重复提交，请稍后再试";

    /**
     * 指定参与防重的参数名（方法形参名）。
     * 为空时：对整个参数列表做摘要（原逻辑）。
     * 非空时：只对这些参数做摘要。
     */
    String[] keys() default {};

    /**
     * 是否使用 SpEL 表达式动态指定 key。
     * 优先级高于 keys，非空时忽略 keys。
     * 例："#dto.orderId + ':' + #dto.userId"
     */
    String keySpel() default "";
}
