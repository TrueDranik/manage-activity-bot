package tg.bot.activity.api.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tg.bot.activity.model.dto.tg.AlbumDto;
import tg.bot.activity.service.files.AlbumService;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/album")
@RequiredArgsConstructor
@Tag(name = "Альбом")
public class AlbumController {

    private final AlbumService albumService;

    @GetMapping
    @Operation(summary = "Получить все альбомы")
    @ResponseStatus(HttpStatus.OK)
    public List<AlbumDto> getAllAlbums() {
        return albumService.getAllAlbums();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить альбом по id")
    @ResponseStatus(HttpStatus.OK)
    public AlbumDto getAllAlbum(@PathVariable(name = "id") Long id) {
        return albumService.getAlbumById(id);
    }
}
