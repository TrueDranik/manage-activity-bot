package tg.bot.activity.repository;

import com.bot.sup.model.entity.ActivityFormat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityFormatRepository extends JpaRepository<ActivityFormat, Long> {
    boolean existsByNameEqualsIgnoreCase(String userAnswer);

    List<ActivityFormat> findActivityFormatsByIsActiveIsTrue();

    ActivityFormat findActivityFormatByNameIgnoreCase(String activityFormatName);

    @Query("select a.name from ActivityFormat a where a.id = ?1 ")
    String getActivityFormatNameById(Long id);

    @Query("select a.icon.id from ActivityFormat a where a.id = ?1 ")
    Long getIconId(Long id);
}
