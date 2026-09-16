package io.github.xuefm.moli.service.impl;

import io.github.xuefm.moli.data.account.LoginUser;
import io.github.xuefm.moli.data.login.LoginRequest;
import io.github.xuefm.moli.data.web.Results;
import io.github.xuefm.moli.data.web.TokenData;
import io.github.xuefm.moli.entity.SysAccount;
import io.github.xuefm.moli.expection.BusinessException;
import io.github.xuefm.moli.service.LoginService;
import io.github.xuefm.moli.service.cache.AccountRedisService;
import io.github.xuefm.moli.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class LoginServiceImpl implements LoginService {

    public final AuthenticationManager authenticationManager;
    public final AccountRedisService accountRedisService;

    @Override
    public Results<String> login(LoginRequest loginRequest) {
        //AuthenticationManager用户认证
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getLoginName(), loginRequest.getLoginPassword());
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        //认证失败返回失败信息
        if (Objects.isNull(authenticate)) {
            throw new BusinessException("账号或密码错误");
        }
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        SysAccount accountInfo = loginUser.getSysAccount();
        //认证成功 生成jwt
        TokenData tokenData = new TokenData();
        tokenData.setAccountId(accountInfo.getId());
        tokenData.setVersion("v1.0.0");
        String token = JwtUtil.createToken(tokenData);


        //把用户信息存入redis
        accountRedisService.setAccount(accountInfo.getId(), loginUser);
        return Results.ofSuccess(token);
    }


    @Override
    public Results<String> logout() {
        //通过SecurityContextHolder获取用户信息
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
        String accountInfoId = (String) usernamePasswordAuthenticationToken.getPrincipal();
        //删除redis真的值
        accountRedisService.delAccount(accountInfoId);
        return Results.success();
    }
}
