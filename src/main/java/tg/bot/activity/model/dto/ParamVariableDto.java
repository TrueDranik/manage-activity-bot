package tg.bot.activity.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParamVariableDto {
    private Long id;
    private String name;
    private String value;
    private String paramType;

}
