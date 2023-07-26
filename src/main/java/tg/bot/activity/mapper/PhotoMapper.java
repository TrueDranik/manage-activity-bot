package tg.bot.activity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tg.bot.activity.model.dto.tg.PhotoDto;
import tg.bot.activity.model.dto.tg.PhotoInfoDto;
import tg.bot.activity.model.entity.Photo;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PhotoMapper extends BaseMapper<Photo, PhotoInfoDto> {

    List<PhotoDto> photoToPhotoDtos(List<Photo> photo);

    @Mapping(target = "name", source = "nameFromRequest")
    PhotoDto photoToPhotoDto(Photo photo);

    @Mapping(target = "name", source = "nameFromRequest")
    PhotoInfoDto domainToDto(Photo photo);

}
