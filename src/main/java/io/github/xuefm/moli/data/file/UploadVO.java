package io.github.xuefm.moli.data.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 上传文件 响应数据
 */
@Data
@AllArgsConstructor
@Schema(description = "上传文件响应数据")
public class UploadVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(title = "桶名", example = "moli")
    private String bucket;

    @Schema(title = "对象名", example = "2026/09/15/xxx.png")
    private String objectName;
}
