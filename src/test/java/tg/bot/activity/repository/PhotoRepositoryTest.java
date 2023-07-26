package tg.bot.activity.repository;

import com.bot.sup.model.entity.Photo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@DisplayName("The repository for the photo should")
@DataJpaTest
@ActiveProfiles("test")
class PhotoRepositoryTest {

    @Autowired
    private PhotoRepository photoRepository;

    @Test
    @Sql("/sql/create-album.sql")
    @Sql("/sql/create-photos.sql")
    @Sql(value = "/sql/delete-from-photo.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = "/sql/delete-from-album.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("find a photo with pagination")
    void findByAlbum_Id() {
        int page = 0;
        int size = 20;
        long totalElements = 21L;
        int albumId = 1;

        Pageable pageable = PageRequest.of(page, size);
        Page<Photo> pageablePhoto = photoRepository.findByAlbumId(1L, pageable);
        Photo firstPhoto = pageablePhoto.getContent().get(0);

        assertEquals(totalElements, pageablePhoto.getTotalElements());
        assertEquals(page, pageablePhoto.getPageable().getPageNumber());
        assertEquals(size, pageablePhoto.getContent().size());
        assertEquals(size, pageablePhoto.getPageable().getPageSize());
        assertEquals(albumId, firstPhoto.getAlbum().getId());
        assertEquals(albumId, firstPhoto.getAlbum().getId());
        assertEquals("Photo 1", firstPhoto.getNameFromRequest());
    }
}