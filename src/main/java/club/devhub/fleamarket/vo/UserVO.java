package club.devhub.fleamarket.vo;

import lombok.Data;

@Data
public class UserVO {
    private Long userId;
    private String username;
    private Integer sex;
    private String address;
}
