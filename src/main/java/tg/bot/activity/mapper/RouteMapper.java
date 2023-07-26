package tg.bot.activity.mapper;

import com.bot.sup.model.dto.tg.RouteDto;
import com.bot.sup.model.entity.Route;
import com.bot.sup.repository.PhotoRepository;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;

@Mapper(componentModel = "spring")
public abstract class RouteMapper implements BaseMapper<Route, RouteDto> {

    @Autowired
    PhotoRepository photoRepository;

    @AfterMapping
    public void setRepoValues(RouteDto dto, @MappingTarget Route route) {
        route.setIsActive(true);
        route.setPhoto(photoRepository.findById(dto.getPhotoId())
                .orElseThrow(() -> new EntityNotFoundException("Photo with id[" + dto.getPhotoId() + "] not found")));
    }

    @AfterMapping
    public void setRepoValues(Route route, @MappingTarget RouteDto dto) {
        if(route.getPhoto() != null ){
            dto.setPhotoId(route.getPhoto().getId());
        } else {
            dto.setPhotoId(null);
        }
    }


}
