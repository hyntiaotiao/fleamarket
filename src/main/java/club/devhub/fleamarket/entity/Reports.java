package club.devhub.fleamarket.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 举报表
 * @TableName t_reports
 */
@Data
public class Reports implements Serializable {
    /**
     * 举报Id

     */
    private Long reportid;

    /**
     * 被举报物品Id

     */
    private Long commodityid;

    /**
     * 举报理由
     */
    private String reason;

    /**
     * 
     */
    private LocalDateTime createTime;

    /**
     * 
     */
    private LocalDateTime updateTime;

    /**
     *举报者Id
     */
    private Long userId;

    /**
     *举报状态
     */
    private Integer auditState;

    private static final long serialVersionUID = 1L;
}