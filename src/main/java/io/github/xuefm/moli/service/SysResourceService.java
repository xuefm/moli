package io.github.xuefm.moli.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.xuefm.moli.data.sys.sysresource.*;
import io.github.xuefm.moli.data.web.PageData;
import io.github.xuefm.moli.data.web.Results;
import io.github.xuefm.moli.entity.SysResource;

import java.util.List;

/**
 * <p>
 * 系统资源 服务类
 * 默认生成CRUD方法
 * </p>
 *
 * @author Author
 * @since 2026-09-15
 */
public interface SysResourceService extends IService<SysResource> {

    Results<PageData<SysResourceVO>> getList(SysResourceGetListRequest sysResourceGetListRequest);

    Results<List<SysResourceVO>> getStepByStep(GetStepByStepRequest getStepByStepRequest);

    Results<String> addResource(AddResourceRequest addResourceRequest);

    Results<String> updateResource(UpdateResourceRequest updateResourceRequest);

    Results<List<SysResourceTreeVO>> treeAll();

    Results<List<String>> getByRoleId(String id);

    /**
     * 删除资源，同时删除资源与角色的关联数据
     */
    Results<String> deleteResource(String id);
}
