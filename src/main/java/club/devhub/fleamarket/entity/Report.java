package club.devhub.fleamarket.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 举报表
 * @TableName t_reports
 */
@Data
public class Report implements Serializable {
    /**
     * 举报Id

     */
    private Long reportId;

    /**
     * 被举报物品Id
     */
    private Long commodityId;

    /**
     * 举报理由
     */
    private String reason;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
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