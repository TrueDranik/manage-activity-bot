package tg.bot.activity.service.paramVariable;

import org.springframework.stereotype.Service;
import tg.bot.activity.model.dto.ParamVariableCreateDto;
import tg.bot.activity.model.dto.ParamVariableDto;

import java.util.List;

@Service
public interface ParamVariableService {

    ParamVariableDto getParamVariableById(Long id);

    List<ParamVariableDto> getParamsByParamType(String paramType);

    ParamVariableDto createParamVariable(ParamVariableCreateDto paramDto);

    ParamVariableDto updateParamVariable(ParamVariableDto paramDto);

    void updateParams(List<ParamVariableDto> params);

    void createParams(List<ParamVariableCreateDto> params);

    void deleteParams(List<Long> paramsId);
}
