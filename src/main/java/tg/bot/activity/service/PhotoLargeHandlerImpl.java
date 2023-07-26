package tg.bot.activity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tg.bot.activity.common.enums.PhotoSizeEnum;
import tg.bot.activity.model.entity.PhotoLargeSize;
import tg.bot.activity.repository.PhotoLargeRepository;
import tg.bot.activity.repository.PhotoRepository;
import tg.bot.activity.util.FeignMinioServiceUtil;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PhotoLargeHandlerImpl implements PhotoHandler {

    private final PhotoRepository photoRepository;
    private final PhotoLargeRepository photoLargeRepository;
    private final FeignMinioServiceUtil minioServiceUtil;

    @Value("${ids.bucket}")
    private String bucketName;

    @Override
    public String getPhotoUrlFromDb(Long id) {
        PhotoLargeSize photo = photoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Photo not found"))
                .getLargePhoto();

        if (photo.getUrl() != null && photo.getExpireUrlDateTime().after(new Timestamp(System.currentTimeMillis()))) {
            return photo.getUrl();
        } else {
            String url = minioServiceUtil.getUrlToUploadFile(photo.getName(), bucketName);
            photo.setUrl(url);
            LocalDateTime expireDateTime = LocalDateTime.now().plusDays(6L)
                    .withHour(23).withMinute(30).withSecond(0).withNano(0);
            photo.setExpireUrlDateTime(Timestamp.valueOf(expireDateTime));
            photoLargeRepository.save(photo);
            return photo.getUrl();
        }
    }

    @Override
    public PhotoSizeEnum getSize() {
        return PhotoSizeEnum.LARGE;
    }
}
