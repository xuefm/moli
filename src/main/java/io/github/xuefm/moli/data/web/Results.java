package io.github.xuefm.moli.data.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder(value = {"status", "message", "data"})
@Schema(description = "统一响应体")
public class Results<T> {
    /**
     * 返回任务状态码，默认 00000
     */
    @Schema(title = "状态码", description = "200 成功；9999 业务失败", example = "200")
    private Integer status;

    /**
     * 任务描述
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(title = "任务描述", example = "请求成功")
    private String message;

    /**
     * 数据集
     */
    @Schema(title = "数据集")
    private T data;


    public interface G {
        Integer SUCCESS_CODE = 200;
        String SUCCESS_MESSAGE = "请求成功";

        Integer FAILURE_CODE = 9999;
        String FAILURE_MESSAGE = "请求失败";
    }

    public static <T> Results<T> success() {
        return new Results<>(G.SUCCESS_CODE, G.SUCCESS_MESSAGE, null);
    }

    public static <T> Results<T> success(T data) {
        return new Results<>(G.SUCCESS_CODE, G.SUCCESS_MESSAGE, data);
    }

    public static <T> Results<T> success(String message) {
        return new Results<>(G.SUCCESS_CODE, message, null);
    }

    public static <T> Results<T> ofSuccess(T data) {
        return new Results<>(G.SUCCESS_CODE, G.SUCCESS_MESSAGE, data);
    }

    public static <T> Results<T> success(T data, String message) {
        return new Results<>(G.SUCCESS_CODE, message, data);
    }

    public static <T> Results<T> failure() {
        return new Results<>(G.FAILURE_CODE, G.FAILURE_MESSAGE, null);
    }

    public static <T> Results<T> failure(Integer status) {
        return new Results<>(status, G.FAILURE_MESSAGE, null);
    }

    public static <T> Results<T> failure(String message) {
        return new Results<>(G.FAILURE_CODE, message, null);
    }

    public static <T> Results<T> failure(String message, T data) {
        return new Results<>(G.FAILURE_CODE, message, data);
    }

    /**
     * 状态判断
     */
    public static <T> boolean isSuccess(Results<T> basisResponse) {
        return G.SUCCESS_CODE.equals(basisResponse.getStatus());
    }

    public static <T> boolean isFailure(Results<T> basisResponse) {
        return !G.SUCCESS_CODE.equals(basisResponse.getStatus());
    }


}
