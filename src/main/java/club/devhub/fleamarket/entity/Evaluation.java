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
    private Long evaluateid;

    /**
     * 被评价物品
     */
    private Long commodityid;

    /**
     * 评价者Id
     */
    private Long userid;

    /**
     * 评价内容
     */
    private String content;

    /**
     * 
     */
    private LocalDateTime createTime;

    /**
     * 
     */
    private LocalDateTime updateTime;

    private static final long serialVersionUID = 1L;
}