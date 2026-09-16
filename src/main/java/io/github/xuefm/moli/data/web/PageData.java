package io.github.xuefm.moli.data.web;

import io.github.xuefm.moli.expection.BusinessException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
@Schema(description = "分页数据")
public class PageData<T> {

    /**
     * 当前页条数
     */
    @Schema(title = "当前页", example = "1")
    private int current;

    /**
     * 每页的条数
     */
    @Schema(title = "每页条数", example = "10")
    private int size;

    /**
     * 数据
     */
    @Schema(title = "当前页数据")
    private List<T> list;


    /**
     * 总条数（list.size()）
     */
    @Schema(title = "总条数", example = "100")
    private long total;


    /**
     * 总页数（计算值）
     */
    @Schema(title = "总页数", example = "10")
    private int totalPages;

    public PageData(int current, int size,long total, List<T> list) {
        if (current <= 0) throw new BusinessException("current必须大于0");
        if (size <= 0) throw new BusinessException("size必须大于0");
        if (Objects.isNull(list)) throw new BusinessException("list不能为null");
        this.current = current;
        this.size = size;
        this.list = list;
        this.total = total;
        this.totalPages =(int)( total % size > 0 ? total / size + 1 : total / size);
    }
}
