package tg.bot.activity.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import tg.bot.activity.model.dto.tg.ScheduleCreateDto;
import tg.bot.activity.model.entity.Schedule;
import tg.bot.activity.repository.ActivityRepository;
import tg.bot.activity.repository.InstructorRepository;
import tg.bot.activity.repository.RouteRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public abstract class ScheduleCreateMapper implements BaseMapper<Schedule, ScheduleCreateDto> {

    @Autowired
    ActivityRepository activityRepository;
    @Autowired
    RouteRepository routeRepository;

    @Autowired
    InstructorRepository instructorRepository;

    @Override
    @Mapping(target = "activityId", expression = "java(domain.getActivity().getId())")
    @Mapping(target = "routeId", expression = "java(domain.getRoute().getId())")
    public abstract ScheduleCreateDto domainToDto(Schedule domain);

    @Override
    @Mapping(target = "amountFreePlaces", source = "participants")
    public abstract Schedule dtoToDomain(ScheduleCreateDto dto);

    @AfterMapping
    public void setRepoValues(ScheduleCreateDto dto, @MappingTarget Schedule schedule) {
        schedule.setActivity(activityRepository.findById(dto.getActivityId())
                .orElseThrow(() -> new EntityNotFoundException("Activity not found")));
        schedule.setRoute(routeRepository.findById(dto.getRouteId())
                .orElseThrow(() -> new EntityNotFoundException("Route not found")));
        schedule.setIsActive(true);
        Set<Instructor> instructor = new HashSet<>();
        for (long id : dto.getInstructorIds()) {
            instructor.add(instructorRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Instructor not found")));
        }
        schedule.setInstructor(instructor);
    }

    @AfterMapping
    public void after(Schedule schedule, @MappingTarget ScheduleCreateDto dto) {
        List<Long> instructorIds = new ArrayList<>();
        for (Instructor instructor : schedule.getInstructor()) {
            instructorIds.add(instructor.getId());
        }
        dto.setInstructorIds(instructorIds);
    }
}
