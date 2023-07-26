package tg.bot.activity.mapper;

import com.bot.sup.model.dto.ParamVariableCreateDto;
import com.bot.sup.model.dto.ParamVariableDto;
import com.bot.sup.model.entity.ParamVariable;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ParamVariableMapper extends BaseMapper<ParamVariable, ParamVariableDto> {

    ParamVariable createDtoToDomain(ParamVariableCreateDto dto);


}
