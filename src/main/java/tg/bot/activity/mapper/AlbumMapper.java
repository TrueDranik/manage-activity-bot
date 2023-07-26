package tg.bot.activity.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import tg.bot.activity.common.enums.PhotoSizeEnum;
import tg.bot.activity.model.dto.tg.AlbumDto;
import tg.bot.activity.model.entity.Album;
import tg.bot.activity.model.entity.Photo;
import tg.bot.activity.repository.PhotoRepository;
import tg.bot.activity.service.files.photo.PhotoService;

@Mapper(componentModel = "spring")
public abstract class AlbumMapper implements BaseMapper<Album, AlbumDto> {

    @Autowired
    PhotoRepository photoRepository;
    @Autowired
    PhotoService photoService;

    @AfterMapping
    public void setAmountPhoto(Album album, @MappingTarget AlbumDto dto) {
        dto.setAmountPhoto(photoRepository.getContByAlbumId(album));
        Photo photo = photoRepository.findFirstByAlbum_Id(album.getId());
        if (photo != null) {
            dto.setCover(photoService.getImageUrl(photo.getId(), PhotoSizeEnum.SMALL));
        } else {
            dto.setCover(null);
        }
    }
}
