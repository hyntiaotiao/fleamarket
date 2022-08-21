package club.devhub.fleamarket.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 
 * @TableName t_commodity
 */
@Data
public class Commodity implements Serializable {
    /**
     * 物品ID
     */
    private Long commodityId;

    /**
     * 物品名称
     */
    private String commodityName;

    /**
     * 物品种类
     */
    private Integer category;

    /**
     * 价格
     * （金额在实体类中表示应该用Long，以“分”为单位，数据库对应类型为bigint）
     */
    private Long price;

    /**
     * 物品的详细说明
     */
    private String message;

    /**
     * 是否已经卖出（默认没有卖出）
     */
    private Integer sold;

    /**
     * 物品主人id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


    private static final long serialVersionUID = 1L;
}