package io.github.xuefm.moli.data.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Schema(title = "GetUrlRequest", description = "获取文件访问地址请求参数")
public class GetUrlRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Schema(title = "桶名", required = true, example = "moli")
    private String bucket;

    @NotNull
    @Schema(title = "对象名", required = true, example = "2026/09/15/xxx.png")
    private String objectName;


}
