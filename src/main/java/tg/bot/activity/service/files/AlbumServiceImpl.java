package tg.bot.activity.service.files;

import com.bot.sup.mapper.AlbumMapper;
import com.bot.sup.model.dto.tg.AlbumDto;
import com.bot.sup.model.entity.Album;
import com.bot.sup.repository.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumServiceImpl implements AlbumService {
    private final AlbumRepository albumRepository;
    private final AlbumMapper albumMapper;

    @Override
    public List<AlbumDto> getAllAlbums() {
        List<Album> albums = albumRepository.findAllByIsActiveTrue();

        return albumMapper.domainsToDtos(albums);
    }

    @Override
    public AlbumDto getAlbumById(Long id) {
        Album selectedAlbum = albumRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Album with id " + id + " not found"));
        return albumMapper.domainToDto(selectedAlbum);
    }

    @Override
    public AlbumDto createAlbum(String name) {
        Album createdAlbum = new Album();
        createdAlbum.setName(name);
        createdAlbum.setIsActive(true);
        createdAlbum.setIsChangeable(true);
        albumRepository.save(createdAlbum);
        return albumMapper.domainToDto(createdAlbum);
    }

    @Override
    public void save(Album album) {
        albumRepository.save(album);
    }

    @Override
    public void deleteAlbum(Long albumName) {
        Album albumToDelete = albumRepository.findById(albumName)
                .orElseThrow(() -> new EntityNotFoundException("Album with id not found"));
        albumToDelete.setIsActive(false);
        albumRepository.save(albumToDelete);
    }
}
