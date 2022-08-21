package club.devhub.fleamarket.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 订单表
 * @TableName t_order
 */
@Data
public class Order implements Serializable {
    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 买方ID
     */
    private Long buyerId;

    /**
     * 物品Id
     */
    private Long commodityId;

    /**
     * 订单状态
     */
    private Integer state;

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