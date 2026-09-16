package io.github.xuefm.moli.data.account;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
@Schema(title = "SysAccountUpdateRequest", description = "系统账号：更新账号请求参数")
public class SysAccountUpdateRequest implements java.io.Serializable{

    private static final long serialVersionUID = 1L;

    @NotNull
    @Schema(title = "账号id", required = true, example = "admin")
    private String id;

    @NotNull
    @Schema(title = "角色List", required = true, example = "[1,2,3]")
    private List<String> roleList;
}
