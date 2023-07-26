package tg.bot.activity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tg.bot.activity.model.entity.ActivityType;

import java.util.List;

@Repository
public interface ActivityTypeRepository extends JpaRepository<ActivityType, Long> {
    boolean existsByNameEqualsIgnoreCase(String userAnswer);

    List<ActivityType> findActivityTypesByIsActiveIsTrue();

    ActivityType findActivityTypeByNameIgnoreCase(String activityTypeName);

    List<ActivityType> findAllByIsActiveTrueAndActivityFormat_Id(Long id);
}
