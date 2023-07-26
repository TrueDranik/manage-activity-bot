package tg.bot.activity.repository;

import com.bot.sup.model.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    List<Album> findAll ();

    List<Album> findAllByIsActiveTrue();

    Optional<Album> findById(Long id);

    Album findByName(String name);

    boolean existsByName (String name);
}
