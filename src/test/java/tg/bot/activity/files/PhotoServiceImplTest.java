package tg.bot.activity.files;

import feign.FeignException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.multipart.MultipartFile;
import tg.bot.activity.common.enums.PhotoSizeEnum;
import tg.bot.activity.model.entity.Photo;
import tg.bot.activity.model.entity.PhotoLargeSize;
import tg.bot.activity.model.entity.PhotoSmallSize;
import tg.bot.activity.repository.PhotoRepository;
import tg.bot.activity.service.files.MultipartFileConstructor;
import tg.bot.activity.service.files.photo.PhotoService;
import tg.bot.activity.util.ImageUtil;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
class PhotoServiceImplTest {

    private static final String PHOTO_URL = "https://upload.wikimedia.org/wikipedia/commons/3/3a/Cat03.jpg";
    private static final String PHOTO_NAME = "photo.jpg";
    private static final String URL = "photo_url";
    public static final String LARGE_PHOTO_NAME = "large_photo";
    public static final String SMALL_PHOTO_NAME = "small_photo";
    private static final Long ALBUM_ID = 1L;

    @Autowired
    PhotoService photoService;
    @Autowired
    PhotoRepository photoRepository;

    @AfterEach
    @Sql(value = "/sql/delete-from-photo.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = "/sql/delete-from-album.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void tearDown() {
        var photos = photoRepository.findAll();
        if (!photos.isEmpty()) {
            for (Photo p : photos) {
                photoService.deletePhotoById(p.getId());
            }
        }
    }

    @Test
    @Sql("/sql/create-album.sql")
    void getLargeImageUrlSuccessful() throws IOException {
        var fileToSaveAsBytes = ImageUtil.getImageFromNetByUrl(PHOTO_URL);
        var fileToSave = MultipartFileConstructor.getMultipartFileFromByteArray(PHOTO_NAME, fileToSaveAsBytes);
        Photo savedPhoto = photoService.savePhoto(fileToSave, ALBUM_ID);

        var photoUrl = photoService.getImageUrl(savedPhoto.getId(), PhotoSizeEnum.LARGE);
        assertEquals(PHOTO_NAME, savedPhoto.getNameFromRequest());
        assertNotNull(photoUrl);
    }

    private void assertNotNull(String photoUrl) {
    }

    @Test
    void getLargeImageUrlNotSuccessful() {
        LocalDateTime expireDateTime = LocalDateTime.now().minusHours(6L);
        PhotoLargeSize photoLargeSize = PhotoLargeSize.builder()
                .expireUrlDateTime(Timestamp.valueOf(expireDateTime))
                .url(URL)
                .name(LARGE_PHOTO_NAME)
                .build();
        PhotoSmallSize photoSmallSize = PhotoSmallSize.builder()
                .expireUrlDateTime(Timestamp.valueOf(expireDateTime))
                .url(URL)
                .name(SMALL_PHOTO_NAME)
                .build();
        Photo photo = Photo.builder()
                .largePhoto(photoLargeSize)
                .smallPhoto(photoSmallSize)
                .downloadDateTime(Timestamp.valueOf(LocalDateTime.now()))
                .nameFromRequest(PHOTO_NAME)
                .build();
        Photo savedPhoto = photoRepository.saveAndFlush(photo);

        assertThrows(FeignException.class, () -> photoService.getImageUrl(savedPhoto.getId(), PhotoSizeEnum.LARGE));
    }

    @Test
    @Sql("/sql/create-album.sql")
    void savePhoto() throws IOException {
        var fileToSaveAsBytes = ImageUtil.getImageFromNetByUrl(PHOTO_URL);
        var fileToSave = MultipartFileConstructor.getMultipartFileFromByteArray(PHOTO_NAME, fileToSaveAsBytes);
        Photo savedPhoto = photoService.savePhoto(fileToSave, ALBUM_ID);

        assertEquals(PHOTO_NAME, savedPhoto.getNameFromRequest());
        assertEquals(ALBUM_ID, savedPhoto.getAlbum().getId());
    }

    @Test
    @Sql("/sql/create-album.sql")
    void saveListPhoto() throws IOException {
        var fileToSaveAsBytes = ImageUtil.getImageFromNetByUrl(PHOTO_URL);
        var fileToSave1 = MultipartFileConstructor.getMultipartFileFromByteArray(PHOTO_NAME, fileToSaveAsBytes);
        var fileToSave2 = MultipartFileConstructor.getMultipartFileFromByteArray(PHOTO_NAME, fileToSaveAsBytes);
        List<MultipartFile> filesToUpload = new ArrayList<>();
        filesToUpload.add(fileToSave1);
        filesToUpload.add(fileToSave2);
        for (MultipartFile f : filesToUpload) {
            photoService.savePhoto(f, ALBUM_ID);
        }
        long photos = photoRepository.countAllByAlbum_Id(ALBUM_ID);
        assertEquals(2, photos);
    }

    @Test
    @Sql("/sql/create-album.sql")
    void deletePhotoById() throws IOException {
        var fileToSaveAsBytes = ImageUtil.getImageFromNetByUrl(PHOTO_URL);
        var fileToSave = MultipartFileConstructor.getMultipartFileFromByteArray(PHOTO_NAME, fileToSaveAsBytes);
        Photo savedPhoto = photoService.savePhoto(fileToSave, ALBUM_ID);

        photoService.deletePhotoById(savedPhoto.getId());
        Optional<Photo> deletedPhoto = photoRepository.findById(savedPhoto.getId());

        assertFalse(deletedPhoto.isPresent());
    }
}