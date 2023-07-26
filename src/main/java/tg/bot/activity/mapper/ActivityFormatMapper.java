package tg.bot.activity.mapper;

import com.bot.sup.model.dto.tg.ActivityFormatDto;
import com.bot.sup.model.entity.ActivityFormat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ActivityFormatMapper extends BaseMapper<ActivityFormat, ActivityFormatDto> {
    @Override
    @Mapping(target = "id", ignore = true)
    ActivityFormat dtoToDomain(ActivityFormatDto activityFormatDto);
    @Override
    @Mapping(target = "albumId", source = "album.id")
    ActivityFormatDto domainToDto(ActivityFormat activityFormat);
}
