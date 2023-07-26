package tg.bot.activity.service.aboutUs;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tg.bot.activity.mapper.AboutUsMapper;
import tg.bot.activity.model.dto.AboutUsDto;
import tg.bot.activity.model.dto.AboutUsUpdateDto;
import tg.bot.activity.model.entity.AboutUs;
import tg.bot.activity.model.entity.Photo;
import tg.bot.activity.repository.AboutUsRepository;
import tg.bot.activity.service.files.photo.PhotoService;
import tg.bot.activity.service.paramVariable.ParamVariableService;

import javax.persistence.EntityNotFoundException;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AboutUsServiceServiceImpl implements AboutUsService {

    private final AboutUsRepository aboutUsRepository;
    private final AboutUsMapper aboutUsMapper;
    private final ParamVariableService paramVariableService;
    private final PhotoService photoService;

    @Override
    public void save(AboutUs aboutUs) {
        aboutUsRepository.save(aboutUs);
    }

    @Override
    public Optional<AboutUs> findById(Long aboutUsId) {
        return aboutUsRepository.findById(aboutUsId);
    }

    @Override
    public AboutUsDto getAboutUs() {
        AboutUs aboutUs = aboutUsRepository.getAboutUs()
                .orElseThrow(() -> new EntityNotFoundException("There is not info about us"));
        return aboutUsMapper.domainToDto(aboutUs);
    }

    @Override
    public AboutUsDto updateInfo(AboutUsUpdateDto dto) {
        Photo photoToDelete = aboutUsRepository.getAboutUs()
                .orElseThrow(() -> new EntityNotFoundException("About us info not found")).getPhoto();
        Long photoIdToDelete = photoToDelete == null ? null : photoToDelete.getId();
        AboutUs updatedAboutUs = aboutUsMapper.updateDtoToDomain(dto);
        updatedAboutUs.setId(1L);

        paramVariableService.updateParams(dto.getParamsToUpdate());
        paramVariableService.createParams(dto.getParamsToCreate());
        paramVariableService.deleteParams(dto.getParamToDelete());
        aboutUsRepository.save(updatedAboutUs);
        if (!Objects.equals(photoIdToDelete, updatedAboutUs.getPhoto().getId()) && photoIdToDelete != null) {
            photoService.deletePhotoById(photoIdToDelete);
        }

        return aboutUsMapper.domainToDto(updatedAboutUs);
    }
}
