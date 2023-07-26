package tg.bot.activity.service.activity;

import com.bot.sup.model.ActivityRequestParams;
import com.bot.sup.model.dto.tg.ActivityDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ActivityService {
    List<ActivityDto> getAllActivity(ActivityRequestParams params);

    ActivityDto getActivityById(Long id);

    ActivityDto createActivity(ActivityDto activityCreateDto);

    ActivityDto updateActivity(Long id, ActivityDto activityCreateDto);

    void deleteActivity(Long id);
}
