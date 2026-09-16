package io.github.xuefm.moli.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.xuefm.moli.data.sys.sysresource.*;
import io.github.xuefm.moli.data.web.PageData;
import io.github.xuefm.moli.data.web.Results;
import io.github.xuefm.moli.entity.SysResource;
import io.github.xuefm.moli.entity.SysRoleResource;
import io.github.xuefm.moli.expection.BusinessException;
import io.github.xuefm.moli.mapper.SysResourceMapper;
import io.github.xuefm.moli.mapper.SysRoleResourceMapper;
import io.github.xuefm.moli.service.SysResourceService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 系统资源 服务实现类
 * </p>
 *
 * @author Author
 * @since 2026-09-15
 */
@Slf4j
@Service
@AllArgsConstructor
public class SysResourceServiceImpl extends ServiceImpl<SysResourceMapper, SysResource>
        implements SysResourceService {

    private final SysResourceMapper sysResourceMapper;
    private final SysRoleResourceMapper sysRoleResourceMapper;

    @Override
    public Results<PageData<SysResourceVO>> getList(SysResourceGetListRequest sysResourceGetListRequest) {
        LambdaQueryWrapper<SysResource> queryWrapper = Wrappers.lambdaQuery(SysResource.class);
        queryWrapper.orderByDesc(SysResource::getCreateTime);
        if (StrUtil.isNotBlank(sysResourceGetListRequest.getTitle())) {
            queryWrapper.like(SysResource::getTitle, sysResourceGetListRequest.getTitle());
        }
        Page<SysResource> page = sysResourceMapper.selectPage(new Page<>(sysResourceGetListRequest.getCurrent(), sysResourceGetListRequest.getSize()), queryWrapper);
        List<SysResource> records = page.getRecords();
        List<SysResourceVO> accountVOList = new ArrayList<>();
        for (SysResource SysResource : records) {
            accountVOList.add(BeanUtil.copyProperties(SysResource, SysResourceVO.class));
        }
        return Results.success(new PageData<>(sysResourceGetListRequest.getCurrent(), sysResourceGetListRequest.getSize(), page.getTotal(), accountVOList));
    }

    @Override
    public Results<List<SysResourceTreeVO>> treeAll() {

        LambdaQueryWrapper<SysResource> queryWrapper = Wrappers.lambdaQuery(SysResource.class);
        queryWrapper.eq(SysResource::getEnabled, 1);
        queryWrapper.eq(SysResource::getLevel, 1);
        List<SysResource> sysResourceList = sysResourceMapper.selectList(queryWrapper);

        List<SysResourceTreeVO> sysResourceTreeVOList = new ArrayList<>();
        for (SysResource sysResource : sysResourceList) {
            SysResourceTreeVO sysResourceTreeVO = new SysResourceTreeVO();
            BeanUtil.copyProperties(sysResource, sysResourceTreeVO);
            sysResourceTreeVOList.add(sysResourceTreeVOSetChildren(sysResourceTreeVO));
        }


        return Results.success(sysResourceTreeVOList);
    }

    private SysResourceTreeVO sysResourceTreeVOSetChildren(SysResourceTreeVO sysResourceTreeVO) {
        if (sysResourceTreeVO.getType() == 0) {
            List<SysResource> sysResourceList = sysResourceMapper.selectList(Wrappers.lambdaQuery(SysResource.class)
                    .eq(SysResource::getSuperiorId, sysResourceTreeVO.getId())
                    .eq(SysResource::getEnabled, 1));
            List<SysResourceTreeVO> sysResourceTreeVOList = new ArrayList<>();
            for (SysResource sysResource : sysResourceList) {
                SysResourceTreeVO childSysResourceTreeVO = new SysResourceTreeVO();
                BeanUtil.copyProperties(sysResource, childSysResourceTreeVO);
                sysResourceTreeVOList.add(sysResourceTreeVOSetChildren(childSysResourceTreeVO));
            }
            sysResourceTreeVO.setChildren(sysResourceTreeVOList);
        }
        return sysResourceTreeVO;
    }


    @Override
    public Results<List<SysResourceVO>> getStepByStep(GetStepByStepRequest getStepByStepRequest) {
        LambdaQueryWrapper<SysResource> sysResourceLambdaQueryWrapper = Wrappers.lambdaQuery(SysResource.class);
        if (StrUtil.isBlank(getStepByStepRequest.getSuperiorId())) {
            sysResourceLambdaQueryWrapper.eq(SysResource::getLevel, 1);
        } else {
            SysResource sysResource = sysResourceMapper.selectById(getStepByStepRequest.getSuperiorId());
            if (Objects.isNull(sysResource))
                throw new BusinessException("资源上级id错误");
            if (sysResource.getType() != 0)
                throw new BusinessException("非资源组id");
            sysResourceLambdaQueryWrapper.eq(SysResource::getSuperiorId, sysResource.getId());
        }
        List<SysResource> sysResourceList = sysResourceMapper.selectList(sysResourceLambdaQueryWrapper);

        List<SysResourceVO> accountVOList = new ArrayList<>();
        for (SysResource SysResource : sysResourceList) {
            SysResourceVO sysResourceVO = BeanUtil.copyProperties(SysResource, SysResourceVO.class);

            if (SysResource.getType() == 0) {
                sysResourceVO.setHasChildren(true);
            } else {
                sysResourceVO.setHasChildren(false);
            }
            accountVOList.add(sysResourceVO);
        }
        return Results.success(accountVOList);

    }

    @Override
    public Results<String> addResource(AddResourceRequest addResourceRequest) {
        SysResource sysResource = new SysResource();
        sysResource.setLevel(1);

        if (StrUtil.isNotBlank(addResourceRequest.getSuperiorId())) {
            SysResource superiorSysResource = sysResourceMapper.selectById(addResourceRequest.getSuperiorId());
            if (Objects.isNull(superiorSysResource)) throw new BusinessException("上级id错误");
            if (superiorSysResource.getType() != 0) throw new BusinessException("上级不是资源组");
            sysResource.setLevel(superiorSysResource.getLevel() + 1);
        }
        Long integer = sysResourceMapper.selectCount(Wrappers.lambdaQuery(SysResource.class)
                .eq(SysResource::getCode, addResourceRequest.getCode()));
        if (integer > 0) throw new BusinessException("code重复请换一个");
        BeanUtil.copyProperties(addResourceRequest, sysResource);

        sysResourceMapper.insert(sysResource);
        return Results.success();
    }

    @Override
    public Results<String> updateResource(UpdateResourceRequest updateResourceRequest) {
        SysResource sysResource = sysResourceMapper.selectById(updateResourceRequest.getId());
        if (Objects.isNull(sysResource)) throw new BusinessException("id错误");
        BeanUtil.copyProperties(updateResourceRequest, sysResource);
        sysResourceMapper.updateById(sysResource);
        return Results.success();
    }

    @Override
    public Results<List<String>> getByRoleId(String id) {
        List<String> sysRoleResourceIdList = sysResourceMapper.getResourceIdListByRoleId(id);
        return Results.success(sysRoleResourceIdList);
    }

    @Override
    @Transactional
    public Results<String> deleteResource(String id) {
        SysResource sysResource = sysResourceMapper.selectById(id);
        if (Objects.isNull(sysResource)) throw new BusinessException("未找到资源");

        // 删除资源
        sysResourceMapper.deleteById(id);
        // 删除资源与角色的关联数据
        sysRoleResourceMapper.delete(Wrappers.lambdaQuery(SysRoleResource.class)
                .eq(SysRoleResource::getResourceId, id));
        return Results.success();
    }
}
