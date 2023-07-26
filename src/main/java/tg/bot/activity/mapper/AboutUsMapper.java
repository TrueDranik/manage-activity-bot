package tg.bot.activity.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import tg.bot.activity.common.enums.ParamTypeEnum;
import tg.bot.activity.model.dto.AboutUsDto;
import tg.bot.activity.model.dto.AboutUsUpdateDto;
import tg.bot.activity.model.entity.AboutUs;
import tg.bot.activity.repository.PhotoRepository;
import tg.bot.activity.service.paramVariable.ParamVariableService;

@Mapper(componentModel = "spring")
public abstract class AboutUsMapper implements BaseMapper<AboutUs, AboutUsDto> {

    @Autowired
    ParamVariableService paramVariableService;
    @Autowired
    PhotoRepository photoRepository;

    public AboutUs updateDtoToDomain(AboutUsUpdateDto dto) {
        if (dto == null) {
            return null;
        }

        AboutUs aboutUs = new AboutUs();

        aboutUs.setFullDescription(dto.getFullDescription());
        aboutUs.setTitle(dto.getTitle());
        if (dto.getPhotoId()!= null) {
            aboutUs.setPhoto(photoRepository.findById(dto.getPhotoId()).get());
        }

        return aboutUs;
    }

    @AfterMapping
    public void setParams(AboutUs aboutUs, @MappingTarget AboutUsDto dto) {
        dto.setParams(paramVariableService.getParamsByParamType(ParamTypeEnum.ABOUTUS.getTitle()));
        if (aboutUs.getPhoto() != null) {
            dto.setPhotoId(aboutUs.getPhoto().getId());
        }
    }
}
