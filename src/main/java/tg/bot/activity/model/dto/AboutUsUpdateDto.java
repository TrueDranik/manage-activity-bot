package tg.bot.activity.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AboutUsUpdateDto {

    private String fullDescription;
    private String title;
    private List<ParamVariableDto> paramsToUpdate;
    private List<ParamVariableCreateDto> paramsToCreate;
    private List<Long> paramToDelete;
    private Long photoId;
}
