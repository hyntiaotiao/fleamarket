package club.devhub.fleamarket.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CommodityParam {
    /**
     * 物品类别
     */
    @NotNull(message = "类别不允许为空")
    @Max(value = 5,message = "类别编号最大为5")
    @Min(value = 0,message = "类别编号最小为0")
    private Integer category;

    /**
     * 物品名称
     */
    @NotBlank(message = "物品名不允许为空")
    @Length(max = 20,message = "名称最长为20")
    private String commodityName;

    /**
     * 物品价格
     */
    @NotNull(message = "价格不允许为空")
    @Min(value = 0,message = "价格最低为0")
    private Integer price;

    /**
     * 物品描述信息
     */
    @Length(max = 255,message = "描述文字最长为255")
    private String message="【物品的主人还没有为物品添加描述信息】";
}
