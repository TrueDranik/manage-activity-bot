package tg.bot.activity.mapper;

import com.bot.sup.common.enums.ParamTypeEnum;
import com.bot.sup.model.dto.AboutUsDto;
import com.bot.sup.model.dto.AboutUsUpdateDto;
import com.bot.sup.model.entity.AboutUs;
import com.bot.sup.repository.PhotoRepository;
import com.bot.sup.service.paramVariable.ParamVariableService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

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
