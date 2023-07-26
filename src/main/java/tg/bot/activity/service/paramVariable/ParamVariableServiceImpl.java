package tg.bot.activity.service.paramVariable;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tg.bot.activity.mapper.ParamVariableMapper;
import tg.bot.activity.model.dto.ParamVariableCreateDto;
import tg.bot.activity.model.dto.ParamVariableDto;
import tg.bot.activity.model.entity.ParamVariable;
import tg.bot.activity.repository.ParamVariableRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParamVariableServiceImpl implements ParamVariableService {

    private final ParamVariableRepository paramVariableRepository;
    private final ParamVariableMapper paramVariableMapper;

    @Override
    public ParamVariableDto getParamVariableById(Long id) {
        ParamVariable param = paramVariableRepository.getParamVariableById(id).orElse(null);
        if (param == null) {
            return null;
        } else {
            return paramVariableMapper.domainToDto(param);
        }
    }

    @Override
    public List<ParamVariableDto> getParamsByParamType(String paramType) {
        List<ParamVariable> paramVariables = paramVariableRepository.findAllByParamType(paramType);
        return paramVariableMapper.domainsToDtos(paramVariables);
    }

    @Override
    public ParamVariableDto createParamVariable(ParamVariableCreateDto paramDto) {
        ParamVariable paramVariable = paramVariableMapper.createDtoToDomain(paramDto);
        paramVariableRepository.save(paramVariable);
        return paramVariableMapper.domainToDto(paramVariable);
    }

    @Override
    public ParamVariableDto updateParamVariable(ParamVariableDto paramDto) {
        ParamVariable paramVariable = paramVariableMapper.dtoToDomain(paramDto);
        paramVariableRepository.save(paramVariable);
        return paramVariableMapper.domainToDto(paramVariable);
    }

    @Override
    public void updateParams(List<ParamVariableDto> params) {
        if (params != null) {
            for (ParamVariableDto param : params) {
                updateParamVariable(param);
            }
        }
    }

    @Override
    public void createParams(List<ParamVariableCreateDto> params) {
        if (params != null) {
            for (ParamVariableCreateDto param : params) {
                createParamVariable(param);
            }
        }

    }

    @Override
    public void deleteParams(List<Long> paramsId) {
        if (paramsId != null) {
            for (Long id : paramsId) {
                paramVariableRepository.deleteById(id);
            }
        }
    }
}
