package tg.bot.activity.service.activity.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tg.bot.activity.mapper.ActivityMapper;
import tg.bot.activity.model.ActivityRequestParams;
import tg.bot.activity.model.dto.tg.ActivityDto;
import tg.bot.activity.model.entity.Activity;
import tg.bot.activity.model.entity.Activity_;
import tg.bot.activity.repository.ActivityRepository;
import tg.bot.activity.repository.ScheduleRepository;
import tg.bot.activity.repository.specification.ActivitySpecification;
import tg.bot.activity.service.activity.ActivityService;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final ScheduleRepository scheduleRepository;
    private final ActivityMapper activityMapper;

    @Override
    public List<ActivityDto> getAllActivity(ActivityRequestParams params) {
        var activities = activityRepository.findAll(new ActivitySpecification(params));
        return activityMapper.domainsToDtos(activities);
    }

    @Override
    public ActivityDto getActivityById(Long id) {
        Activity activityById = findActivityById(id);

        return activityMapper.domainToDto(activityById);
    }

    @Override
    public ActivityDto createActivity(ActivityDto activityDto) {
        Activity activity = activityMapper.dtoToDomain(activityDto);
        activityRepository.save(activity);

        return activityMapper.domainToDto(activity);
    }

    @Override
    public ActivityDto updateActivity(Long id, ActivityDto activityDto) {
        Activity activityForUpdate = activityMapper.dtoToDomain(activityDto);
        Activity activityFromDb = findActivityById(id);

        BeanUtils.copyProperties(activityForUpdate, activityFromDb, Activity_.ID, Activity_.IS_ACTIVE);
        activityRepository.save(activityFromDb);

        return activityMapper.domainToDto(activityFromDb);
    }

    @Transactional
    @Override
    public void deleteActivity(Long id) {
        Activity activityById = findActivityById(id);
        activityById.setIsActive(false);
        activityRepository.save(activityById);

        scheduleRepository.setScheduleInactiveByActivityId(id);
    }

    private Activity findActivityById(Long id) {
        return activityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Activity with id[" + id + "] not found"));
    }
}
