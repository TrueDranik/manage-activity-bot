package tg.bot.activity.repository;

import com.bot.sup.model.entity.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityTypeRepository extends JpaRepository<ActivityType, Long> {
    boolean existsByNameEqualsIgnoreCase(String userAnswer);

    List<ActivityType> findActivityTypesByIsActiveIsTrue();

    ActivityType findActivityTypeByNameIgnoreCase(String activityTypeName);

    List<ActivityType> findAllByIsActiveTrueAndActivityFormat_Id(Long id);
}
