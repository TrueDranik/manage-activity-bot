package tg.bot.activity.service.files.photo;

import com.bot.sup.common.enums.PhotoSizeEnum;
import com.bot.sup.model.entity.Photo;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

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
