package tg.bot.activity.service.activity.type;

import org.springframework.stereotype.Service;
import tg.bot.activity.model.dto.tg.ActivityTypeDto;

import java.util.List;

@Service
public interface ActivityTypeService {

    List<ActivityTypeDto> getAllActivityTypeByActivityFormatId(Long formatId);

    List<ActivityTypeDto> getAllActivityType();

    ActivityTypeDto getActivityTypeById(Long id);

    ActivityTypeDto createActivityType(ActivityTypeDto activityTypeCreateDto);

    ActivityTypeDto updateActivityType(Long id, ActivityTypeDto activityTypeCreateDto);

    void deleteActivityType(Long id);
}
