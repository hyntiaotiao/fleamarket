package club.devhub.fleamarket.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 举报返回模板
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportVO {
    /**
     * 举报Id
     * */
    private Long reportId;
    /**
     * 举报者
     * */
    private Long userId;

    /**
     * 被举报者
     * */
    private Long reportedId;
    /**
     * 被举报物品
     * */
    private Long commodityId;
    /**
     * 举报理由
     * */
    private String reason;
    /**
     * 物品描述
     * */
    private String message;

    /**
     * 订单创建时间
     * */
    private String createTime;
}
