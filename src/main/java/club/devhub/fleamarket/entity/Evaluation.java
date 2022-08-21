package club.devhub.fleamarket.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 评价表
 * @TableName t_evaluation
 */
@Data
public class Evaluation implements Serializable {
    /**
     * 评价Id
     */
    private Long evaluationId;

    /**
     * 被评价物品
     */
    private Long commodityId;

    /**
     * 评价内容
     */
    private String content;

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