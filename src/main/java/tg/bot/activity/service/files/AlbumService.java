package tg.bot.activity.service.files;

import tg.bot.activity.model.dto.tg.AlbumDto;
import tg.bot.activity.model.entity.Album;

import java.util.List;

public interface AlbumService {

    List<AlbumDto> getAllAlbums();

    AlbumDto getAlbumById(Long id);

    AlbumDto createAlbum(String name);

    void save(Album album);

    void deleteAlbum(Long albumName);
}
