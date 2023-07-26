package tg.bot.activity.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import tg.bot.activity.model.dto.tg.ActivityDto;
import tg.bot.activity.model.entity.Activity;
import tg.bot.activity.repository.ActivityFormatRepository;
import tg.bot.activity.repository.ActivityTypeRepository;

import javax.persistence.EntityNotFoundException;

@Mapper(componentModel = "spring")
public abstract class ActivityMapper implements BaseMapper<Activity, ActivityDto> {

    @Autowired
    ActivityTypeRepository activityTypeRepository;
    @Autowired
    ActivityFormatRepository activityFormatRepository;

    @AfterMapping
    public void setRepoValues(ActivityDto dto, @MappingTarget Activity activity) {
        activity.setIsActive(true);
        activity.setActivityType(activityTypeRepository.findById(dto.getActivityType().getId())
                .orElseThrow(() -> new EntityNotFoundException("Activity type not found")));
        activity.setActivityFormat(activityFormatRepository.findById(dto.getActivityFormat().getId())
                .orElseThrow(() -> new EntityNotFoundException("Activity format not found")));
    }
}
