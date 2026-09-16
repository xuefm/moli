package io.github.xuefm.moli.handler;

import io.github.xuefm.moli.data.web.Results;
import io.github.xuefm.moli.expection.BusinessException;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 全局异常拦截
 */
@Hidden
@RestControllerAdvice
@Component
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Results handleBindException(BindException e) {
        log.info("处理BindException：{}", e.getMessage());
        List<FieldError> fieldErrors = e.getFieldErrors();
        Map<String, String> fieldErrorMap = fieldErrors.stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        return Results.failure("参数错误", fieldErrorMap);
    }


    @ExceptionHandler(BusinessException.class)
    public Results businessExceptionHandler(BusinessException e) {
        log.error(e.getMessage(), e);
        return Results.failure(e.getMessage());
    }

    /**
     * //@PreAuthorize 注解的异常，抛出 AccessDeniedException 异常，不会被 accessDeniedHandler 捕获，而是会被全局异常捕获
     * @param e
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Results accessDeniedExceptionHandler(AccessDeniedException e){
        log.error(e.getMessage(),e);
        return new Results<>(HttpStatus.FORBIDDEN.value(), "拒绝访问",null);
    }


    @ExceptionHandler(RuntimeException.class)
    public Results runtimeExceptionHandler(RuntimeException e){
        log.error(e.getMessage(),e);
        return Results.failure(e.getMessage());
    }


}
