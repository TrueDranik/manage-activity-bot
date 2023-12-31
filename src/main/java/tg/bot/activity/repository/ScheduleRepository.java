package tg.bot.activity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tg.bot.activity.model.entity.Schedule;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long>, JpaSpecificationExecutor<Schedule> {

    List<Schedule> getSchedulesByActivity_ActivityFormat_Id(Long id);

    Schedule getSchedulesById(Long id);

    @Modifying
    @Query("SELECT s FROM Schedule s " +
            "INNER JOIN Activity a on s.activity.id = a.id " +
            "INNER JOIN Route r on s.route.id = r.id " +
            "WHERE a.activityFormat.id = ?1 and s.eventDate = ?2")
    List<Schedule> selectScheduleByActivityFormatIdAndEventDate(Long id, LocalDate eventDate);

    @Modifying
    @Query("update Schedule s set s.isActive = false where s.activity.id = :id ")
    void setScheduleInactiveByActivityId(Long id);

    @Modifying
    @Query("update Schedule s set s.isActive = false where s.route.id = :id ")
    void setScheduleInactiveByRouteId(Long id);
}
