package club.devhub.fleamarket.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class EvaluateParam {
    @NotBlank(message = "评价不能为空")
    @Length(max = 255,message = "评价内容最大长度为255")
    private String evaluation;

}
