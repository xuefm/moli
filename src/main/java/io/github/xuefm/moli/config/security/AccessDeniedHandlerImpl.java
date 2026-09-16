package io.github.xuefm.moli.config.security;

import io.github.xuefm.moli.data.web.Results;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 注意 使用GlobalExceptionHandler全局异常拦截器
 *
 * //@PreAuthorize 注解的异常，抛出 AccessDeniedException 异常，不会被 accessDeniedHandler 捕获，而是会被全局异常捕获
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        mapper.writeValue(response.getWriter(), Results.failure("拒绝访问"));
    }
}
