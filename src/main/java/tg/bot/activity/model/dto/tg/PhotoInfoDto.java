package tg.bot.activity.model.dto.tg;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class PhotoInfoDto {

    private Long id;
    private String name;
}
