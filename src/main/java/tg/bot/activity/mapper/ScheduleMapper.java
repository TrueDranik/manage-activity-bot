package tg.bot.activity.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import tg.bot.activity.model.dto.tg.ScheduleDto;
import tg.bot.activity.model.entity.Route;
import tg.bot.activity.model.entity.Schedule;

@Mapper(componentModel = "spring", uses = InstructorMapper.class)
public interface ScheduleMapper extends BaseMapper<Schedule, ScheduleDto> {

    @AfterMapping
    default void after(@MappingTarget ScheduleDto scheduleDto) {
        Route route = scheduleDto.getRoute();
        route.setPhoto(null);
        scheduleDto.setRoute(route);
    }
}
