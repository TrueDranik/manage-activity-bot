package tg.bot.activity.model.dto.tg;

import lombok.Getter;
import lombok.Setter;
import tg.bot.activity.model.entity.Activity;
import tg.bot.activity.model.entity.Route;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
public class ScheduleDto {

    private Long id;
    private Activity activity;
    private LocalDate eventDate;
    private LocalTime eventTime;
    private Integer participants;
    private Route route;
    private Set<InstructorDto> instructor;
    private Boolean isActive;
}
