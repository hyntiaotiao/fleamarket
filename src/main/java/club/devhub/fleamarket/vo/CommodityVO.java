package club.devhub.fleamarket.vo;

import lombok.Data;

@Data
public class CommodityVO {
    private String commodityName;
    private Integer category;
    private Long userId;
    private int price;
    private String message;
    private int sold;
    private String createTime;
}
