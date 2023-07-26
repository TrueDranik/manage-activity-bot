package tg.bot.activity.model.dto.tg;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RouteWithoutImageDto {
    private Long id;
    private String name;
    private String startPointCoordinates;
    private String startPointName;
    private String finishPointCoordinates;
    private String finishPointName;
    private String mapLink;
    private String length;
    private Boolean isActive;
}
