package tg.bot.activity.service.activity.format;

import org.springframework.stereotype.Service;
import tg.bot.activity.model.dto.tg.ActivityFormatDto;
import tg.bot.activity.model.entity.ActivityFormat;

import java.util.List;

@Service
public interface ActivityFormatService {

    void save(ActivityFormat activityFormat);

    List<ActivityFormatDto> getAllActivityFormat();

    ActivityFormatDto getActivityFormatById(Long id);

    ActivityFormatDto createActivityFormat(ActivityFormatDto activityFormatCreateDto);

    ActivityFormatDto updateActivityFormat(Long id, ActivityFormatDto activityFormatCreateDto);

    void deleteActivityFormat(Long id);

    String getActivityFormatNameById(Long id);

    Long getIconId(Long activityFormatId);
}
