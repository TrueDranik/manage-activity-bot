package tg.bot.activity.mapper;

import org.mapstruct.Mapper;
import tg.bot.activity.model.dto.ParamVariableCreateDto;
import tg.bot.activity.model.dto.ParamVariableDto;
import tg.bot.activity.model.entity.ParamVariable;

@Mapper(componentModel = "spring")
public interface ParamVariableMapper extends BaseMapper<ParamVariable, ParamVariableDto> {

    ParamVariable createDtoToDomain(ParamVariableCreateDto dto);
}
