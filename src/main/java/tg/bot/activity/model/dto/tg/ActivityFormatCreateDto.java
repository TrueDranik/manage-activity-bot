package tg.bot.activity.model.dto.tg;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(title = "Формат активности")
public class ActivityFormatCreateDto {
    @Schema(title = "Именование формата", defaultValue = "Format")
    private String name;
    @Schema(title = "Доп. информация для формата", nullable = true, defaultValue = " ")
    private String description;
    private Boolean isActive;
}
