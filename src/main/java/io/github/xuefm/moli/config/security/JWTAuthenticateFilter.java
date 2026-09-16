package io.github.xuefm.moli.config.security;

import cn.hutool.core.util.StrUtil;
import io.github.xuefm.moli.data.account.LoginUser;
import io.github.xuefm.moli.data.web.TokenData;
import io.github.xuefm.moli.entity.SysAccount;
import io.github.xuefm.moli.service.cache.AccountRedisService;
import io.github.xuefm.moli.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@AllArgsConstructor
public class JWTAuthenticateFilter extends OncePerRequestFilter {

    public final AccountRedisService accountRedisService;



    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // 排除不需要过滤的路径
        return path.equals("/auth/user/login") ||
                path.startsWith("/public/") ||
                path.contains("swagger") ||
                path.contains("api-docs");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String token = request.getHeader("Authorization");
        if (StrUtil.isBlank(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        //解析token
        boolean verification = JwtUtil.verification(token);
        if (!verification){
            log.info("认证失败");
            filterChain.doFilter(request, response);
            return;
        }
        TokenData tokenData = JwtUtil.resolveTobTokenData(token);


        //获取用户信息
        LoginUser loginUser = accountRedisService.getAccount(tokenData.getAccountId());
        SysAccount accountInfo = loginUser.getSysAccount();
        //存入SecurityContextHolder
        //获取权限封装authorities
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(accountInfo.getId(), null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        filterChain.doFilter(request, response);
    }
}
