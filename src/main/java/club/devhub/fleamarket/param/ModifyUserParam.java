package club.devhub.fleamarket.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ModifyUserParam {

    /**
     * 性别
     */
    @NotNull(message = "性别不能为空")
    @Max(value = 2,message = "性别最大为2")
    @Min(value = 0,message = "性别最大为0")
    private Integer sex;

    /**
     * 地址
     */
    @NotBlank(message = "地址不能为空")
    @Length(max = 255,message = "地址长度最大为255")
    private String address;
}
