package io.github.xuefm.moli.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.xuefm.moli.data.account.*;
import io.github.xuefm.moli.data.web.PageData;
import io.github.xuefm.moli.data.web.Results;
import io.github.xuefm.moli.entity.SysAccount;
import io.github.xuefm.moli.entity.SysAccountRole;
import io.github.xuefm.moli.expection.BusinessException;
import io.github.xuefm.moli.mapper.SysAccountMapper;
import io.github.xuefm.moli.mapper.SysAccountRoleMapper;
import io.github.xuefm.moli.service.SysAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Author
 * @since 2026-09-15
 */
@Slf4j
@Service
@AllArgsConstructor
public class SysAccountServiceImpl extends ServiceImpl<SysAccountMapper, SysAccount>
        implements SysAccountService {

    private final SysAccountMapper accountInfoMapper;
    private final SysAccountRoleMapper sysAccountRoleMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Results<PageData<AccountVO>> getList(SysAccountGetListRequest request) {
        LambdaQueryWrapper<SysAccount> queryWrapper = Wrappers.lambdaQuery(SysAccount.class);
        queryWrapper.orderByDesc(SysAccount::getCreateTime);
        if (StrUtil.isNotBlank(request.getLoginName())) {
            queryWrapper.like(SysAccount::getLoginName, request.getLoginName());
        }
        Page<SysAccount> page = accountInfoMapper.selectPage(new Page<>(request.getCurrent(), request.getSize()), queryWrapper);
        List<SysAccount> records = page.getRecords();
        List<AccountVO> accountVOList = new ArrayList<>();
        for (SysAccount accountInfo : records) {
            accountVOList.add(BeanUtil.copyProperties(accountInfo, AccountVO.class));
        }
        return Results.success(new PageData<>(request.getCurrent(), request.getSize(), page.getTotal(), accountVOList));
    }

    @Override
    public Results<AccountDetailsVO> getDetailsById(String id) {
        SysAccount sysSysAccount = accountInfoMapper.selectById(id);
        AccountDetailsVO accountDetailsVO = BeanUtil.copyProperties(sysSysAccount, AccountDetailsVO.class);
        List<SysAccountRole> sysAccountRoles = sysAccountRoleMapper.selectList(Wrappers.lambdaQuery(SysAccountRole.class)
                .eq(SysAccountRole::getAccountId, id));
        List<String> roleList = sysAccountRoles.stream().map(SysAccountRole::getRoleId).collect(Collectors.toList());
        accountDetailsVO.setRoleList(roleList);
        return Results.success(accountDetailsVO);
    }

    @Override
    @Transactional
    public Results<?> createSysAccount(CreateSysAccountRequest request) {
        SysAccount sysSysAccount = accountInfoMapper.getByLoginName(request.getLoginName());
        if (Objects.nonNull(sysSysAccount))
            throw new BusinessException("登录名已存在，请换一个");

        sysSysAccount=new SysAccount();
        sysSysAccount.setLoginName(request.getLoginName());
        sysSysAccount.setLoginPassword(passwordEncoder.encode(request.getLoginPassword()));
        accountInfoMapper.insert(sysSysAccount);
        for (String roleId : request.getRoleList()) {
            SysAccountRole sysAccountRole = new SysAccountRole();
            sysAccountRole.setAccountId(sysSysAccount.getId());
            sysAccountRole.setRoleId(roleId);
            sysAccountRoleMapper.insert(sysAccountRole);
        }
        return Results.success();
    }

    @Override
    @Transactional
    public Results<?> sysAccountUpdate(SysAccountUpdateRequest request) {
        SysAccount sysSysAccount = accountInfoMapper.selectById(request.getId());
        if (Objects.isNull(sysSysAccount)) throw new BusinessException("未找到账号");

        List<SysAccountRole> sysAccountRoles = sysAccountRoleMapper.selectList(Wrappers.lambdaQuery(SysAccountRole.class)
                .eq(SysAccountRole::getAccountId, sysSysAccount.getId()));
        Map<String, String> sysAccountRolesMap = sysAccountRoles.stream().collect(Collectors.toMap(SysAccountRole::getRoleId, SysAccountRole::getId));

        for (String roleId : request.getRoleList()) {
            String id = sysAccountRolesMap.get(roleId);
            if (Objects.isNull(id)) {
                SysAccountRole sysAccountRole = new SysAccountRole();
                sysAccountRole.setAccountId(sysSysAccount.getId());
                sysAccountRole.setRoleId(roleId);
                sysAccountRoleMapper.insert(sysAccountRole);
            } else {
                sysAccountRolesMap.remove(roleId);
            }
        }
        sysAccountRolesMap.forEach((k, v) -> {
            sysAccountRoleMapper.deleteById(v);
        });
        return Results.success();
    }

    @Override
    @Transactional
    public Results<String> deleteAccount(String id) {
        SysAccount sysSysAccount = accountInfoMapper.selectById(id);
        if (Objects.isNull(sysSysAccount)) throw new BusinessException("未找到账号");

        // 删除账号
        accountInfoMapper.deleteById(id);
        // 删除账号与角色的关联数据
        sysAccountRoleMapper.delete(Wrappers.lambdaQuery(SysAccountRole.class)
                .eq(SysAccountRole::getAccountId, id));
        return Results.success();
    }
}
