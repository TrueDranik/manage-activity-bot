package tg.bot.activity.model.dto.tg;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityFormatDto {

    private Long id;
    private String name;
    private String description;
    private Boolean isActive;
    private Long albumId;
}
