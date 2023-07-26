package tg.bot.activity.service.paramVariable;

import com.bot.sup.model.dto.ParamVariableCreateDto;
import com.bot.sup.model.dto.ParamVariableDto;
import org.springframework.stereotype.Service;

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
