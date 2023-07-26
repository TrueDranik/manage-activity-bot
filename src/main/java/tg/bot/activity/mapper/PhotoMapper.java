package tg.bot.activity.mapper;

import com.bot.sup.model.dto.tg.PhotoDto;
import com.bot.sup.model.dto.tg.PhotoInfoDto;
import com.bot.sup.model.entity.Photo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PhotoMapper extends  BaseMapper<Photo, PhotoInfoDto>{
    List<PhotoDto> photoToPhotoDtos(List<Photo> photo);

    @Mapping(target = "name", source = "nameFromRequest")
    PhotoDto photoToPhotoDto(Photo photo);

    @Mapping(target = "name", source = "nameFromRequest")
    PhotoInfoDto domainToDto (Photo photo);

}
