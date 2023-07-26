package tg.bot.activity.model.dto.tg;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlbumDto {
    private Long id;
    private String name;
    private Integer amountPhoto;
    private String cover;
    private Boolean isActive;
    private Boolean isChangeable;
}
