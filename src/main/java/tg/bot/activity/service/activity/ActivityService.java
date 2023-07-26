package tg.bot.activity.service.activity;

import org.springframework.stereotype.Service;
import tg.bot.activity.model.ActivityRequestParams;
import tg.bot.activity.model.dto.tg.ActivityDto;

import java.util.List;

@Service
public interface ActivityService {

    List<ActivityDto> getAllActivity(ActivityRequestParams params);

    ActivityDto getActivityById(Long id);

    ActivityDto createActivity(ActivityDto activityCreateDto);

    ActivityDto updateActivity(Long id, ActivityDto activityCreateDto);

    void deleteActivity(Long id);
}
