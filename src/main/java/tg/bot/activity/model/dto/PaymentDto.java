package tg.bot.activity.model.dto;

import com.bot.sup.common.enums.PaymentTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDto {
    private long id;
    private PaymentTypeEnum type;
    private String description;
    private String data;
}
