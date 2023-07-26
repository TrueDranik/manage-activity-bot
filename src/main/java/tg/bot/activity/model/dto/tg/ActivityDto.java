package tg.bot.activity.model.dto.tg;

import lombok.Getter;
import lombok.Setter;
import tg.bot.activity.model.entity.ActivityFormat;
import tg.bot.activity.model.entity.ActivityType;

import java.math.BigDecimal;

@Getter
@Setter
public class ActivityDto {

    private Long id;
    private String name;
    private String seasonality;
    private ActivityFormat activityFormat;
    private ActivityType activityType;
    private String description;
    private String duration;
    private String age;
    private String complexity;
    private BigDecimal price;
    private Boolean isActive;
    private int prepayPercent;
}
