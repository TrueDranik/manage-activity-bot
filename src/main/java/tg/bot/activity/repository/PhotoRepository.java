package tg.bot.activity.repository;

import com.bot.sup.model.entity.Album;
import com.bot.sup.model.entity.Photo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    Optional<Photo> findById(Long id);
    @Query("SELECT i FROM Photo i INNER JOIN Route r ON i.id = r.photo.id WHERE r.id = ?1")
    Optional<Photo> findByRouteId(Long id);

    @Query("select p.nameFromRequest from Photo p where p.id = ?1")
    String getNameById(Long id);

    Page<Photo> findByAlbumId(Long id, Pageable pageable);

    Photo findFirstByAlbum_Id(Long id);

    @Query("select count(p.id) from Photo p  where p.album = ?1")
    Integer getContByAlbumId(Album album);
    Long countAllByAlbum_Id(Long albumId);

    @Query(value = "select  photo.id from  photo  order by photo.download_date_time desc limit 1 ", nativeQuery = true)
    Long findLastPhotoId();

}
