package tg.bot.activity.repository;

import com.bot.sup.model.entity.PhotoSmallSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoSmallRepository extends JpaRepository<PhotoSmallSize, Long> {
}
