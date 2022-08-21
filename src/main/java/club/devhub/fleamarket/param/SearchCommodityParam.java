package club.devhub.fleamarket.param;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 物品分页参数（与PagingParam配合使用）
 */
@Data
public class SearchCommodityParam {
    /**
     * 用户Id
     * 默认值null，表示没有按照userId筛选
     */
    @Min(1)
    private Long userId;
    /**
     * 物品类别
     */
    @Max(value = 5,message = "类别编号最大为5")
    @Min(value = 0,message = "类别编号最小为0")
    private Integer category;

    /**
     * 是否卖出
     */
    @Max(value = 1,message = "参数sold最大为1")
    @Min(value = 0,message = "参数sold最小为0")
    private Integer sold;


}