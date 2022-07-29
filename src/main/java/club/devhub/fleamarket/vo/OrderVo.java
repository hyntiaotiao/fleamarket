package club.devhub.fleamarket.vo;


import lombok.Data;

@Data
public class OrderVo {
    private Long orderId;
    private String commodityName;
    private Integer commodityId;
    private Integer category;
    private Integer price;
    private Integer state;
    private Long buyerId;
    private Long sellerId;
    private String createTime;
    private String updateTime;
}
