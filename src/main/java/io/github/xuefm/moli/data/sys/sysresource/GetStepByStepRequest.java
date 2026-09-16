package io.github.xuefm.moli.data.sys.sysresource;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 逐级获取资源
 */
@Data
@Schema(title = "GetStepByStepRequest", description = "系统账号：逐级获取资源请求参数")
public class GetStepByStepRequest {


    @Schema(title = "上级id", required = false, example = "146461616146")
    private String superiorId;
}
