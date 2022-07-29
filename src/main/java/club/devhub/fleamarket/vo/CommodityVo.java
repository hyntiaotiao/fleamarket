package club.devhub.fleamarket.vo;

import lombok.Data;

@Data
public class CommodityVo {
    private String commodityName;
    private Integer category;
    private Long userId;
    private int price;
    private String message;
    private int sold;
    private String createTime;
}
