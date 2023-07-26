package tg.bot.activity.api.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tg.bot.activity.common.enums.PhotoSizeEnum;
import tg.bot.activity.mapper.PhotoMapper;
import tg.bot.activity.model.dto.tg.PhotoDto;
import tg.bot.activity.model.dto.tg.PhotoInfoDto;
import tg.bot.activity.model.entity.Photo;
import tg.bot.activity.service.files.photo.PhotoService;

import java.io.IOException;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
@Tag(name = "Изображения")
public class PhotoController {
    private final PhotoService photoService;
    private final PhotoMapper photoMapper;

    @GetMapping("/{id}/{size}")
    @Operation(summary = "Получить ссылку на картинку по id")
    public String getImageUrlById(@PathVariable Long id, @PathVariable PhotoSizeEnum size) {
        return photoService.getImageUrl(id, size);
    }

    @GetMapping("/name/{id}")
    @Operation(summary = "Получить имя картинки по её id")
    @ResponseStatus(HttpStatus.OK)
    public String getImageName(@PathVariable Long id) {
        return photoService.getImageNameById(id);
    }

    @GetMapping("/album/{id}/{photoSize}")
    @Operation(summary = "получить все фотографии по id альбома")
    @ResponseStatus(HttpStatus.OK)
    public Page<PhotoDto> getAllPhotosByAlbumId(@PathVariable Long id, @ParameterObject Pageable pageable,
                                                @PathVariable PhotoSizeEnum photoSize) {
        List<Photo> photos = photoService.findPhotosByAlbumId(id, pageable);
        List<PhotoDto> photoDtos = photoMapper.photoToPhotoDtos(photos);
        for (PhotoDto photoDto : photoDtos) {
            photoDto.setUrl(photoService.getImageUrl(photoDto.getId(), photoSize));
        }
        int total = photoDtos.size();
        return new PageImpl<>(photoDtos, pageable, total);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить фото по id")
    @ResponseStatus(HttpStatus.OK)
    public void deletePhoto(@RequestParam("listId") List<Long> listId) {
        photoService.deleteListPhotos(listId);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Загрузить картинки в бд")
    public void uploadListPhotos(@RequestBody List<MultipartFile> files, @RequestParam("albumId") Long albumId) throws IOException {
        photoService.uploadListPhotos(files, albumId);
    }

    @PostMapping(path = "/photo",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Загрузить картинку в бд")
    public PhotoInfoDto uploadPhoto(@RequestBody MultipartFile file, @RequestParam("albumId") Long albumId) throws IOException {
        Photo photo = photoService.savePhoto(file, albumId);
        return photoMapper.domainToDto(photo);
    }
}
