package tg.bot.activity.mapper;

import com.bot.sup.model.dto.tg.ActivityTypeDto;
import com.bot.sup.model.entity.ActivityType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ActivityTypeMapper extends BaseMapper<ActivityType, ActivityTypeDto> {
    @Override
    @Mapping(target = "id", ignore = true)
    ActivityType dtoToDomain(ActivityTypeDto activityTypeDto);
}
