package tg.bot.activity.service.files.photo;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import tg.bot.activity.common.enums.PhotoSizeEnum;
import tg.bot.activity.model.entity.Photo;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface PhotoService {

    Photo savePhoto(MultipartFile file, Long albumId) throws IOException;

    String getImageNameById(Long id);

    List<Photo> findPhotosByAlbumId(Long albumId, Pageable pageable);

    String getImageUrl(Long id, PhotoSizeEnum size);

    Optional<Photo> findPhotoByRouteId(Long routeId);

    void deletePhotoById(Long id);

    void deleteListPhotos(List<Long> listId);

    void uploadListPhotos(List<MultipartFile> files, Long albumId) throws IOException;
}
