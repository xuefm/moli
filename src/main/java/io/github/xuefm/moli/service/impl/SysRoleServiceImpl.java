package io.github.xuefm.moli.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.xuefm.moli.data.sys.sysrole.CreateSysRoleRequest;
import io.github.xuefm.moli.data.sys.sysrole.SysRoleGetListRequest;
import io.github.xuefm.moli.data.sys.sysrole.SysRoleVO;
import io.github.xuefm.moli.data.sys.sysrole.UpdateSysRoleRequest;
import io.github.xuefm.moli.data.web.PageData;
import io.github.xuefm.moli.data.web.Results;
import io.github.xuefm.moli.entity.SysAccountRole;
import io.github.xuefm.moli.entity.SysRole;
import io.github.xuefm.moli.entity.SysRoleResource;
import io.github.xuefm.moli.expection.BusinessException;
import io.github.xuefm.moli.mapper.SysAccountMapper;
import io.github.xuefm.moli.mapper.SysAccountRoleMapper;
import io.github.xuefm.moli.mapper.SysRoleMapper;
import io.github.xuefm.moli.mapper.SysRoleResourceMapper;
import io.github.xuefm.moli.service.SysRoleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统角色 服务实现类
 * </p>
 *
 * @author Author
 * @since 2026-09-15
 */
@Slf4j
@Service
@AllArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole>
        implements SysRoleService {

    private final SysRoleMapper sysRoleMapper;
    private final SysRoleResourceMapper sysRoleResourceMapper;
    private final SysAccountRoleMapper sysAccountRoleMapper;


    @Override
    public Results<PageData<SysRoleVO>> getList(SysRoleGetListRequest sysRoleGetListRequest) {
        LambdaQueryWrapper<SysRole> sysRoleLambdaQueryWrapper = Wrappers.lambdaQuery(SysRole.class);

        if (StrUtil.isNotBlank(sysRoleGetListRequest.getTitle())) {
            sysRoleLambdaQueryWrapper.like(SysRole::getTitle, sysRoleGetListRequest.getTitle());
        }
        Page<SysRole> sysRolePage = sysRoleMapper.selectPage(new Page<>(sysRoleGetListRequest.getCurrent(), sysRoleGetListRequest.getSize()), sysRoleLambdaQueryWrapper);
        List<SysRole> sysRoleList = sysRolePage.getRecords();
        List<SysRoleVO> sysRoleVOList = new ArrayList<>();
        for (SysRole sysRole : sysRoleList) {
            sysRoleVOList.add(BeanUtil.copyProperties(sysRole, SysRoleVO.class));
        }
        return Results.success(new PageData<>(sysRoleGetListRequest.getCurrent(), sysRoleGetListRequest.getSize(), sysRolePage.getTotal(), sysRoleVOList));
    }

    @Override
    public Results<List<SysRole>> getAll() {
        List<SysRole> sysRoles = sysRoleMapper.selectList(Wrappers.lambdaQuery(SysRole.class)
                .eq(SysRole::getEnabled, 1));
        return Results.success(sysRoles);
    }

    @Override
    @Transactional
    public Results<String> createRole(CreateSysRoleRequest createSysRoleRequest) {
        SysRole sysRole = sysRoleMapper.selectOne(Wrappers.lambdaQuery(SysRole.class)
                .eq(SysRole::getTitle, createSysRoleRequest.getTitle()));
        if (Objects.nonNull(sysRole))
            throw new BusinessException("角色已存在，请换一个名字");
        sysRole = new SysRole();
        sysRole.setTitle(createSysRoleRequest.getTitle());
        sysRole.setCode(createSysRoleRequest.getCode());

        sysRoleMapper.insert(sysRole);

        for (String resourceId : createSysRoleRequest.getResourceList()) {
            SysRoleResource sysRoleResource = new SysRoleResource();
            sysRoleResource.setRoleId(sysRole.getId());
            sysRoleResource.setResourceId(resourceId);
            sysRoleResourceMapper.insert(sysRoleResource);
        }
        return Results.success();
    }

    @Override
    @Transactional
    public Results<String> updateRole(UpdateSysRoleRequest updateSysRoleRequest) {
        SysRole sysRole = sysRoleMapper.selectById(updateSysRoleRequest.getId());
        if (Objects.isNull(sysRole)) throw new BusinessException("id错误");
        sysRole.setTitle(updateSysRoleRequest.getTitle());
        sysRole.setCode(updateSysRoleRequest.getCode());
        sysRoleMapper.updateById(sysRole);
        List<SysRoleResource> sysRoleResources = sysRoleResourceMapper.selectList(Wrappers.lambdaQuery(SysRoleResource.class)
                .eq(SysRoleResource::getRoleId, sysRole.getId()));
        Map<String, String> sysRoleResourceMap = sysRoleResources.stream().collect(Collectors.toMap(SysRoleResource::getResourceId, SysRoleResource::getId));

        for (String resourceId : updateSysRoleRequest.getResourceList()) {
            String id = sysRoleResourceMap.get(resourceId);
            if (Objects.isNull(id)) {
                SysRoleResource sysRoleResource = new SysRoleResource();
                sysRoleResource.setRoleId(sysRole.getId());
                sysRoleResource.setResourceId(resourceId);
                sysRoleResourceMapper.insert(sysRoleResource);
            } else {
                sysRoleResourceMap.remove(resourceId);
            }

        }
        sysRoleResourceMap.forEach((k, v) -> {
            sysRoleResourceMapper.deleteById(v);
        });

        return Results.success();
    }

    @Override
    @Transactional
    public Results<String> deleteRole(String id) {
        SysRole sysRole = sysRoleMapper.selectById(id);
        if (Objects.isNull(sysRole)) throw new BusinessException("未找到角色");

        // 删除角色
        sysRoleMapper.deleteById(id);
        // 删除角色与资源的关联数据
        sysRoleResourceMapper.delete(Wrappers.lambdaQuery(SysRoleResource.class)
                .eq(SysRoleResource::getRoleId, id));
        // 删除角色与账号的关联数据
        sysAccountRoleMapper.delete(Wrappers.lambdaQuery(SysAccountRole.class)
                .eq(SysAccountRole::getRoleId, id));
        return Results.success();
    }
}
