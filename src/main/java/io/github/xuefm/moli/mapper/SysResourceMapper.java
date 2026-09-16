package io.github.xuefm.moli.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.xuefm.moli.entity.SysResource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统资源 Mapper 接口
 * </p>
 *
 * @author Author
 * @since 2026-09-15
 */
@Mapper
public interface SysResourceMapper extends BaseMapper<SysResource> {

    List<String> getCodeByAccountId(@Param("accountId") String accountId);

    /**
     * 通过角色id获取资源列表
     * @param roleId
     * @return
     */
    List<String> getResourceIdListByRoleId(@Param("roleId") String roleId);

}
