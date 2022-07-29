package club.devhub.fleamarket.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;


@Data
public class ReportCommodityParam {
    @NotBlank
    @Length(max = 255,message = "举报理由最大长度为255")
    private String reason;
}
