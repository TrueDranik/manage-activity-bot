package tg.bot.activity.service.activity.type.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tg.bot.activity.mapper.ActivityTypeMapper;
import tg.bot.activity.model.dto.tg.ActivityTypeDto;
import tg.bot.activity.model.entity.ActivityType;
import tg.bot.activity.model.entity.ActivityType_;
import tg.bot.activity.repository.ActivityTypeRepository;
import tg.bot.activity.service.activity.type.ActivityTypeService;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityTypeServiceImpl implements ActivityTypeService {

    private final ActivityTypeRepository activityTypeRepository;
    private final ActivityTypeMapper activityTypeMapper;

    public void save(ActivityType activityType) {
        activityTypeRepository.save(activityType);
    }

    public boolean existsByNameEqualsIgnoreCase(String userAnswer) {
        return activityTypeRepository.existsByNameEqualsIgnoreCase(userAnswer);
    }

    public List<ActivityType> findAll() {
        return activityTypeRepository.findActivityTypesByIsActiveIsTrue();
    }

    public ActivityType findActivityTypeByNameIgnoreCase(String nameActivityType) {
        return activityTypeRepository.findActivityTypeByNameIgnoreCase(nameActivityType);
    }

    @Override
    public List<ActivityTypeDto> getAllActivityTypeByActivityFormatId(Long formatId) {
        List<ActivityType> activityTypes = activityTypeRepository.findAllByIsActiveTrueAndActivityFormat_Id(formatId);
        return activityTypeMapper.domainsToDtos(activityTypes);
    }

    @Override
    public List<ActivityTypeDto> getAllActivityType() {
        List<ActivityType> activityTypes = findAll();

        return activityTypeMapper.domainsToDtos(activityTypes);
    }

    @Override
    public ActivityTypeDto getActivityTypeById(Long id) {
        ActivityType activityTypeById = findActivityTypeById(id);

        return activityTypeMapper.domainToDto(activityTypeById);
    }

    @Override
    public ActivityTypeDto createActivityType(ActivityTypeDto activityTypeDto) {
        ActivityType activityType = activityTypeMapper.dtoToDomain(activityTypeDto);
        activityTypeRepository.save(activityType);

        return activityTypeMapper.domainToDto(activityType);
    }

    @Override
    public ActivityTypeDto updateActivityType(Long id, ActivityTypeDto activityTypeDto) {
        ActivityType activityTypeToUpdate = activityTypeMapper.dtoToDomain(activityTypeDto);
        ActivityType activityTypeFromDb = findActivityTypeById(id);

        BeanUtils.copyProperties(activityTypeToUpdate, activityTypeFromDb, ActivityType_.ID);
        activityTypeRepository.save(activityTypeFromDb);

        return activityTypeMapper.domainToDto(activityTypeFromDb);
    }

    @Override
    public void deleteActivityType(Long id) {
        ActivityType activityTypeById = findActivityTypeById(id);
        activityTypeById.setIsActive(false);

        activityTypeRepository.save(activityTypeById);
    }

    public ActivityType findActivityTypeById(Long id) {
        return activityTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Activity type with id[" + id + "] not found"));
    }
}
