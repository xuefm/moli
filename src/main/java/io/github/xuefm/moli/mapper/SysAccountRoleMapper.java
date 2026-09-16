package io.github.xuefm.moli.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.xuefm.moli.entity.SysAccountRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 系统账号 系统角色 关联表(多对多) Mapper 接口
 * </p>
 *
 * @author Author
 * @since 2026-09-15
 */
@Mapper
public interface SysAccountRoleMapper extends BaseMapper<SysAccountRole> {

}
