package tg.bot.activity.service.files.photo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tg.bot.activity.common.enums.PhotoSizeEnum;
import tg.bot.activity.model.entity.Album;
import tg.bot.activity.model.entity.Photo;
import tg.bot.activity.model.entity.PhotoLargeSize;
import tg.bot.activity.model.entity.PhotoSmallSize;
import tg.bot.activity.repository.AlbumRepository;
import tg.bot.activity.repository.PhotoRepository;
import tg.bot.activity.service.CompressService;
import tg.bot.activity.service.PhotoFactory;
import tg.bot.activity.service.PhotoHandler;
import tg.bot.activity.service.files.MultipartFileConstructor;
import tg.bot.activity.util.FeignMinioServiceUtil;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    @Autowired
    private final FeignMinioServiceUtil feignMinioServiceUtil;
    private final AlbumRepository albumRepository;
    private final PhotoRepository photoRepository;
    private final CompressService compressService;

    @Value("${ids.bucket}")
    private String bucketName;

    public Optional<Photo> findPhotoByRouteId(Long routeId) {
        return photoRepository.findByRouteId(routeId);
    }

    @Override
    @Transactional
    public Photo savePhoto(MultipartFile fileFromRequest, Long albumId) throws IOException {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new EntityNotFoundException("Album with id[%s] not found".formatted(albumId)));
        PhotoSmallSize smallPhoto = saveSmallPhoto(fileFromRequest, albumId);
        PhotoLargeSize largePhoto = saveLargePhoto(fileFromRequest, albumId);

        Photo photo = Photo.builder()
                .downloadDateTime(new Timestamp(System.currentTimeMillis()))
                .nameFromRequest(fileFromRequest.getOriginalFilename())
                .album(album)
                .smallPhoto(smallPhoto)
                .largePhoto(largePhoto)
                .build();
        return photoRepository.save(photo);
    }

    private PhotoSmallSize saveSmallPhoto(MultipartFile fileFromRequest, Long albumId) throws IOException {
        String originalFileName = Objects.requireNonNull(fileFromRequest.getOriginalFilename());
        String fileName = createFileNameToUpload(albumId, originalFileName, PhotoSizeEnum.SMALL);
        String fileExtension = getFileExtension(originalFileName);
        byte[] fileAsBytes = compressService.tryToScale(fileFromRequest, fileExtension);
        MultipartFile fileToUpload = MultipartFileConstructor.getMultipartFileFromByteArray(fileName, fileAsBytes);
        uploadPhoto(fileToUpload);
        return PhotoSmallSize.builder()
                .name(fileName)
                .build();
    }

    private PhotoLargeSize saveLargePhoto(MultipartFile fileFromRequest, Long albumId) {
        String fileName = createFileNameToUpload(albumId, Objects.requireNonNull(fileFromRequest.getOriginalFilename()), PhotoSizeEnum.LARGE);
        MultipartFile fileToUpload = MultipartFileConstructor.getNewFile(fileName, fileFromRequest);
        uploadPhoto(fileToUpload);
        return PhotoLargeSize.builder()
                .name(fileName)
                .build();
    }

    private String createFileNameToUpload(Long albumId, String oldFileName, PhotoSizeEnum photoSize) {
        var photoIdentity = LocalDateTime.now();
        String fileExtension = getFileExtension(oldFileName);
        return "album" + albumId + "_" + photoIdentity + "_" + photoSize.toString() + "." + fileExtension;
    }

    private String getFileExtension(String fileNme) {
        var splittedFileName = fileNme.split("\\.");
        return splittedFileName[splittedFileName.length - 1];
    }

    public void uploadPhoto(MultipartFile file) {
        feignMinioServiceUtil.handleFileUpload(bucketName, file);
    }

    @Override
    @Transactional
    public String getImageUrl(Long id, PhotoSizeEnum size) {
        PhotoHandler photoHandler = PhotoFactory.getPhotoHandler(size);
        return photoHandler.getPhotoUrlFromDb(id);
    }

    @Override
    @Transactional
    public void deletePhotoById(Long id) {
        String nameToDeleteSmall = photoRepository.getReferenceById(id).getSmallPhoto().getName();
        String nameToDeleteLarge = photoRepository.getReferenceById(id).getLargePhoto().getName();
        feignMinioServiceUtil.deleteUrlToUploadFile(nameToDeleteSmall, bucketName);
        feignMinioServiceUtil.deleteUrlToUploadFile(nameToDeleteLarge, bucketName);

        photoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteListPhotos(List<Long> listId) {
        for (Long id : listId) {
            deletePhotoById(id);
        }
    }

    @Override
    @Transactional
    public void uploadListPhotos(List<MultipartFile> files, Long albumId) throws IOException {
        for (MultipartFile file : files) {
            savePhoto(file, albumId);
        }
    }

    @Override
    public List<Photo> findPhotosByAlbumId(Long albumId, Pageable pageable) {
        return photoRepository.findByAlbumId(albumId, pageable).getContent();
    }

    @Override
    public String getImageNameById(Long id) {
        return photoRepository.getNameById(id);
    }
}
