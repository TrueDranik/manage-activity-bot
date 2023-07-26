package tg.bot.activity.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AboutUsDto {
    private Long id;
    private String fullDescription;
    private String title;
    private List<ParamVariableDto> params;
    private Long photoId;
}
