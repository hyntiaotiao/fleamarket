package club.devhub.fleamarket.param;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 分页参数
 */
@Data
public class PagingParam {
    /**
     * 当前页码
     * 没有写@NotNull,允许前端不填,因为提供了默认值
     */
    @Min(value = 1, message = "页码最小为1")
    private Integer current = 1;
    /**
     * 每页显示几条
     * 没有写@NotNull,允许前端不填,因为提供了默认值
     */
    @Min(value = 1, message = "单页条数最小为1")
    @Max(value = 100, message = "单页条数最大为100")
    private Integer pageSize = 10;

}