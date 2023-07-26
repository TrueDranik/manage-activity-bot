package tg.bot.activity.service.files;

import com.bot.sup.model.dto.tg.AlbumDto;
import com.bot.sup.model.entity.Album;

import java.util.List;

public interface AlbumService {

    List<AlbumDto> getAllAlbums();

    AlbumDto getAlbumById(Long id);

    AlbumDto createAlbum(String name);

    void save(Album album);

    void deleteAlbum(Long albumName);
}
