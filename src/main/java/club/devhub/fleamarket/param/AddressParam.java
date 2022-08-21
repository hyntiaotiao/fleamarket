package club.devhub.fleamarket.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class AddressParam {
    /**
     * 购买人地址
     */
    @NotBlank
    @Length(max = 255,message = "描述文字最长为255")
    private String address;
}
