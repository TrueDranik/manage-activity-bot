package tg.bot.activity.mapper;

import com.bot.sup.model.dto.tg.ScheduleDto;
import com.bot.sup.model.entity.Route;
import com.bot.sup.model.entity.Schedule;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = InstructorMapper.class)
public interface ScheduleMapper extends BaseMapper<Schedule, ScheduleDto> {

    @AfterMapping
    default void after(@MappingTarget ScheduleDto scheduleDto) {
        Route route = scheduleDto.getRoute();
        route.setPhoto(null);
        scheduleDto.setRoute(route);
    }
}
