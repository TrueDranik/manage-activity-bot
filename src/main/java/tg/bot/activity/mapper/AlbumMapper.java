package tg.bot.activity.mapper;

import com.bot.sup.common.enums.PhotoSizeEnum;
import com.bot.sup.model.dto.tg.AlbumDto;
import com.bot.sup.model.entity.Album;
import com.bot.sup.model.entity.Photo;
import com.bot.sup.repository.PhotoRepository;
import com.bot.sup.service.files.photo.PhotoService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

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
