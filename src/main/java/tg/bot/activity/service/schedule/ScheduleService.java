package tg.bot.activity.service.schedule;

import com.bot.sup.model.RouteActivityRequestParams;
import com.bot.sup.model.dto.tg.ScheduleCreateDto;
import com.bot.sup.model.dto.tg.ScheduleDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public interface ScheduleService {
    ScheduleDto getScheduleByTelegramId(Long telegramId);

    List<ScheduleDto> getAllSchedule(RouteActivityRequestParams params);

    ScheduleDto getScheduleById(Long id);

    List<ScheduleCreateDto> createSchedule(List<ScheduleCreateDto> createDto);

    ScheduleCreateDto updateSchedule(Long id, ScheduleCreateDto scheduleCreateDto);

    List<ScheduleDto> getAllFilteredSchedule(Boolean isActive, LocalDate eventDate, LocalTime eventTime);

    void deleteSchedule(Long id);

    int getFrePlacesByScheduleId(Long id);
}
