package club.devhub.fleamarket.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class UserParam {
    @NotBlank
    @Length(max = 255,message = "用户名长度最大为255")
    private String username;
    @NotBlank
    @Length(max = 20,message = "密码长度最大为20")
    private String password;
}
