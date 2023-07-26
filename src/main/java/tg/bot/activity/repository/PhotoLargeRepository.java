package tg.bot.activity.repository;

import com.bot.sup.model.entity.PhotoLargeSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoLargeRepository extends JpaRepository<PhotoLargeSize, Long> {
}
