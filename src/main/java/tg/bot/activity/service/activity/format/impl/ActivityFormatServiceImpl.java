package tg.bot.activity.service.activity.format.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tg.bot.activity.mapper.ActivityFormatMapper;
import tg.bot.activity.model.dto.tg.ActivityFormatDto;
import tg.bot.activity.model.entity.ActivityFormat;
import tg.bot.activity.repository.ActivityFormatRepository;
import tg.bot.activity.service.activity.format.ActivityFormatService;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityFormatServiceImpl implements ActivityFormatService {

    private final ActivityFormatRepository activityFormatRepository;
    private final ActivityFormatMapper activityFormatMapper;

    @Override
    public void save(ActivityFormat activityFormat) {
        activityFormatRepository.save(activityFormat);
    }

    public boolean existsByNameEqualsIgnoreCase(String userAnswer) {
        return activityFormatRepository.existsByNameEqualsIgnoreCase(userAnswer);
    }

    public List<ActivityFormat> findAll() {
        return activityFormatRepository.findActivityFormatsByIsActiveIsTrue();
    }

    public ActivityFormat findActivityFormatByNameIgnoreCase(String activityFormatName) {
        return activityFormatRepository.findActivityFormatByNameIgnoreCase(activityFormatName);
    }

    @Override
    public List<ActivityFormatDto> getAllActivityFormat() {
        List<ActivityFormat> activityFormats = findAll();

        return activityFormatMapper.domainsToDtos(activityFormats);
    }

    @Override
    public ActivityFormatDto getActivityFormatById(Long id) {
        ActivityFormat activityFormatById = findActivityFormatById(id);

        return activityFormatMapper.domainToDto(activityFormatById);
    }

    @Override
    public ActivityFormatDto createActivityFormat(ActivityFormatDto activityFormatDto) {
        ActivityFormat activityFormat = activityFormatMapper.dtoToDomain(activityFormatDto);
        activityFormatRepository.save(activityFormat);

        return activityFormatMapper.domainToDto(activityFormat);
    }

    @Override
    public ActivityFormatDto updateActivityFormat(Long id, ActivityFormatDto activityFormatDto) {
        ActivityFormat activityToUpdate = activityFormatMapper.dtoToDomain(activityFormatDto);
        ActivityFormat activityFromDb = findActivityFormatById(id);

        BeanUtils.copyProperties(activityToUpdate, activityFromDb, ActivityFormat_.ID);
        activityFormatRepository.save(activityFromDb);

        return activityFormatMapper.domainToDto(activityFromDb);
    }

    @Override
    public void deleteActivityFormat(Long id) {
        ActivityFormat activityFormat = findActivityFormatById(id);
        activityFormat.setIsActive(false);
        activityFormat.setIcon(null);

        activityFormatRepository.save(activityFormat);
    }

    @Override
    public String getActivityFormatNameById(Long id) {
        return activityFormatRepository.getActivityFormatNameById(id);
    }

    @Override
    public Long getIconId(Long activityFormatId) {
        return activityFormatRepository.getIconId(activityFormatId);
    }

    public ActivityFormat findActivityFormatById(Long id) {
        return activityFormatRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Activity format with id[" + id + "] not found"));
    }
}
