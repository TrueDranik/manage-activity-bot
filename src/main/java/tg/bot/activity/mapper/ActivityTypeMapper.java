package tg.bot.activity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tg.bot.activity.model.dto.tg.ActivityTypeDto;
import tg.bot.activity.model.entity.ActivityType;

@Mapper(componentModel = "spring")
public interface ActivityTypeMapper extends BaseMapper<ActivityType, ActivityTypeDto> {

    @Override
    @Mapping(target = "id", ignore = true)
    ActivityType dtoToDomain(ActivityTypeDto activityTypeDto);
}
