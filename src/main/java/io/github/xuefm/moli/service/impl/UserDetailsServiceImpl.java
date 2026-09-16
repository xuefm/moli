package io.github.xuefm.moli.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.github.xuefm.moli.data.account.LoginUser;
import io.github.xuefm.moli.entity.SysAccount;
import io.github.xuefm.moli.expection.BusinessException;
import io.github.xuefm.moli.mapper.SysAccountMapper;
import io.github.xuefm.moli.mapper.SysResourceMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SysAccountMapper sysAccountMapper;
    private final SysResourceMapper sysResourceMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.查询用户
        SysAccount accountInfo = sysAccountMapper.selectOne(Wrappers.lambdaQuery(SysAccount.class)
                .eq(SysAccount::getLoginName, username));
        if (Objects.isNull(accountInfo)) {
            throw new BusinessException("用户不存在");
        }
        if (accountInfo.getEnabled() != 1) {
            throw new BusinessException("账号未启用");
        }
        //2.查询权限
        List<String> code = sysResourceMapper.getCodeByAccountId(accountInfo.getId());
        //3.封装UserDetails对象返回
        LoginUser loginUser = new LoginUser(accountInfo, code);
        return loginUser;
    }
}
