package tg.bot.activity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tg.bot.activity.model.dto.tg.ActivityFormatDto;
import tg.bot.activity.model.entity.ActivityFormat;

@Mapper(componentModel = "spring")
public interface ActivityFormatMapper extends BaseMapper<ActivityFormat, ActivityFormatDto> {

    @Override
    @Mapping(target = "id", ignore = true)
    ActivityFormat dtoToDomain(ActivityFormatDto activityFormatDto);

    @Override
    @Mapping(target = "albumId", source = "album.id")
    ActivityFormatDto domainToDto(ActivityFormat activityFormat);
}
