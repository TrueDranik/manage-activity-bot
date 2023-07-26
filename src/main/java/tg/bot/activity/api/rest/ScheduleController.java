package tg.bot.activity.api.rest;

import com.bot.sup.model.RouteActivityRequestParams;
import com.bot.sup.model.dto.tg.ScheduleCreateDto;
import com.bot.sup.model.dto.tg.ScheduleDto;
import com.bot.sup.service.schedule.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/schedule")
@Tag(name = "Расписание")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @GetMapping("/telegramId/{telegramId}")
    @Operation(summary = "Получить расписание, выбранное пользователем")
    public ScheduleDto getScheduleByTelegramId(@PathVariable("telegramId") Long telegramId) {
        return scheduleService.getScheduleByTelegramId(telegramId);
    }

    @GetMapping
    @Operation(summary = "Получить список всех расписаний")
    public List<ScheduleDto> getAllSchedule(@ParameterObject RouteActivityRequestParams params) {
        return scheduleService.getAllSchedule(params);
    }

    @GetMapping("/scheduleParams")
    @Operation(summary = "Получить будущие расписания от заданного времени")
    public List<ScheduleDto> getAllFilteredSchedule(@RequestParam(value = "active") Boolean isActive,
                                                    @RequestParam(value = "date") String eventDate,
                                                    @RequestParam(value = "time") String eventTime) {
        return scheduleService.getAllFilteredSchedule(isActive, LocalDate.parse(eventDate), LocalTime.parse(eventTime));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить расписание по id")
    public ScheduleDto getScheduleById(@PathVariable("id") Long id) {
        return scheduleService.getScheduleById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Создать новое расписание")
    public List<ScheduleCreateDto> createSchedule(@RequestBody List<ScheduleCreateDto> scheduleCreateDto) {
        return scheduleService.createSchedule(scheduleCreateDto);
    }

    @PutMapping("{id}")
    @Operation(summary = "Изменить существующее расписание")
    public ScheduleCreateDto updateSchedule(@PathVariable(name = "id") Long id,
                                            @RequestBody ScheduleCreateDto scheduleCreateDto) {
        return scheduleService.updateSchedule(id, scheduleCreateDto);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Удалить существующее расписание")
    public void deleteSchedule(@PathVariable(name = "id") Long id) {
        scheduleService.deleteSchedule(id);
    }
}
