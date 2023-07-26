package tg.bot.activity.model.dto;

import lombok.Getter;
import lombok.Setter;
import tg.bot.activity.common.enums.PaymentTypeEnum;

@Getter
@Setter
public class PaymentDto {

    private long id;
    private PaymentTypeEnum type;
    private String description;
    private String data;
}
