package tg.bot.activity.service.schedule.impl;

import com.bot.sup.exception.NotValidParamParticipantsException;
import com.bot.sup.mapper.ScheduleCreateMapper;
import com.bot.sup.mapper.ScheduleMapper;
import com.bot.sup.model.RouteActivityRequestParams;
import com.bot.sup.model.ScheduleParams;
import com.bot.sup.model.dto.tg.ScheduleCreateDto;
import com.bot.sup.model.dto.tg.ScheduleDto;
import com.bot.sup.model.entity.Schedule;
import com.bot.sup.model.entity.Schedule_;
import com.bot.sup.model.entity.SelectedSchedule;
import com.bot.sup.repository.ScheduleRepository;
import com.bot.sup.repository.SelectedScheduleRepository;
import com.bot.sup.repository.specification.ScheduleFilterByDateAndTimeForDaySpecification;
import com.bot.sup.repository.specification.ScheduleFilterByDateSpecification;
import com.bot.sup.repository.specification.ScheduleSpecification;
import com.bot.sup.service.schedule.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.persistence.OptimisticLockException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final SelectedScheduleRepository selectedScheduleRepository;
    private final ScheduleMapper scheduleMapper;
    private final ScheduleCreateMapper scheduleCreateMapper;

    public void saveSelectedSchedule(SelectedSchedule selectedSchedule) {
        selectedScheduleRepository.save(selectedSchedule);
    }

    public void deleteSelectedScheduleByTelegramId(Long telegramId) {
        selectedScheduleRepository.deleteByTelegramId(telegramId);
    }

    public List<Schedule> getSchedulesByActivityFormatId(Long activityFormatId) {
        return scheduleRepository.getSchedulesByActivity_ActivityFormat_Id(activityFormatId);
    }

    public List<Schedule> findScheduleByActivityFormatIdAndEventDate(Long activityFormatId, LocalDate eventDate) {
        return scheduleRepository
                .selectScheduleByActivityFormatIdAndEventDate(activityFormatId, eventDate);
    }

    public SelectedSchedule findSelectedScheduleByTelegramId(Long telegramId) {
        return selectedScheduleRepository.findByTelegramId(telegramId)
                .orElseThrow(() -> new EntityNotFoundException("User with Telegram id[" + telegramId + "] didn't selected schedule"));
    }

    @Override
    public ScheduleDto getScheduleByTelegramId(Long telegramId) {
        Optional<SelectedSchedule> byTelegramId = Optional.ofNullable(findSelectedScheduleByTelegramId(telegramId));

        Schedule schedule = null;
        if (byTelegramId.isPresent()) {
            Long scheduleId = byTelegramId.get().getCurrentScheduleId();

            schedule = findScheduleById(scheduleId);
        }

        return scheduleMapper.domainToDto(schedule);
    }

    @Override
    public List<ScheduleDto> getAllSchedule(RouteActivityRequestParams params) {
        List<Schedule> scheduleDtos = scheduleRepository.findAll(new ScheduleSpecification(params));

        return scheduleMapper.domainsToDtos(scheduleDtos);
    }

    @Override
    public ScheduleDto getScheduleById(Long id) {
        Schedule scheduleById = findScheduleById(id);

        return scheduleMapper.domainToDto(scheduleById);
    }

    @Transactional
    @Override
    public List<ScheduleCreateDto> createSchedule(List<ScheduleCreateDto> scheduleCreateDto) {
        if (scheduleCreateDto == null) {
            return Collections.emptyList();
        }

        List<Schedule> schedules = scheduleCreateMapper.dtosToDomains(scheduleCreateDto);
        scheduleRepository.saveAll(schedules);

        return scheduleCreateMapper.domainsToDtos(schedules);
    }

    @Transactional
    @Override
    public ScheduleCreateDto updateSchedule(Long id, ScheduleCreateDto scheduleCreateDto) {
        Schedule scheduleForUpdate = scheduleCreateMapper.dtoToDomain(scheduleCreateDto);
        Schedule scheduleFromDb = findScheduleById(id);
        int oldParticipants = scheduleFromDb.getParticipants();

        BeanUtils.copyProperties(scheduleForUpdate, scheduleFromDb, Schedule_.ID, Schedule_.IS_ACTIVE,
                Schedule_.AMOUNT_FREE_PLACES, Schedule_.BOOKING);

        scheduleFromDb.setAmountFreePlaces(calculateUpdatedFreePlaces(oldParticipants,
                scheduleCreateDto.getParticipants(), scheduleFromDb.getAmountFreePlaces()));
        try {
            scheduleRepository.save(scheduleFromDb);
        } catch (OptimisticLockException ex) {
            throw new RuntimeException("Something went wrong while updating schedule. Please try later");
        }

        return scheduleCreateMapper.domainToDto(scheduleFromDb);
    }

    private int calculateUpdatedFreePlaces(int oldParticipants, int updatedParticipants, int freePlaces) {
        validChangingParticipants(freePlaces, oldParticipants, updatedParticipants);
        if (updatedParticipants > oldParticipants) {
            return freePlaces + (updatedParticipants - oldParticipants);
        }
        return freePlaces - (oldParticipants - updatedParticipants);
    }

    private void validChangingParticipants(long freePlaces, int oldParticipants, int updatedParticipants) {
        if (updatedParticipants < oldParticipants && (oldParticipants - updatedParticipants > freePlaces)) {
            throw new NotValidParamParticipantsException("Participants can't be less than booked places");
        }

    }

    @Override
    public List<ScheduleDto> getAllFilteredSchedule(Boolean isActive, LocalDate eventDate, LocalTime eventTime) {
        ScheduleParams scheduleParams = new ScheduleParams(isActive, eventDate, eventTime);
        List<ScheduleDto> schedules = getFilteredByTimeSchedulesForCurrentDate(scheduleParams);
        schedules.addAll(getFilteredSchedulesFromNextDay(scheduleParams));

        return schedules;
    }

    @Transactional
    @Override
    public void deleteSchedule(Long id) {
        Schedule scheduleById = findScheduleById(id);
        scheduleById.setIsActive(false);

        scheduleRepository.save(scheduleById);
    }

    @Override
    public int getFrePlacesByScheduleId(Long id) {
        Schedule schedule = scheduleRepository.getReferenceById(id);
        return schedule.getAmountFreePlaces();
    }

    private List<ScheduleDto> getFilteredByTimeSchedulesForCurrentDate(ScheduleParams scheduleParams) {
        List<Schedule> schedules = scheduleRepository.findAll(new ScheduleFilterByDateAndTimeForDaySpecification(scheduleParams));
        return scheduleMapper.domainsToDtos(schedules);
    }

    private List<ScheduleDto> getFilteredSchedulesFromNextDay(ScheduleParams scheduleParams) {
        scheduleParams.setEventDate(scheduleParams.getEventDate().plusDays(1));
        List<Schedule> schedules = scheduleRepository.findAll(new ScheduleFilterByDateSpecification(scheduleParams));
        return scheduleMapper.domainsToDtos(schedules);

    }

    public Schedule findScheduleById(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schedule with id[" + id + "] not found"));
    }
}
