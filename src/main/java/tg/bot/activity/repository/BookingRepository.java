package tg.bot.activity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tg.bot.activity.model.entity.Booking;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findBookingByScheduleId(Long scheduleId);

    Booking findBookingById(Long id);

    Integer countAllByScheduleIdAndPaymentStatus(Long scheduleId, String paymentStatus);

    @Query("select b from Booking b where b.schedule.id = :scheduleId and b.paymentStatus = :paymentStatus")
    List<Booking> findBookingByScheduleIdByPaymentStatus(@Param("scheduleId") Long scheduleId, @Param("paymentStatus") String paymentStatus);

    @Query("select b from Booking b where b.schedule.id = :scheduleId and  b.isActive = true ")
    List<Booking> findBookingByScheduleIdAndActiveIsTrue(@Param("scheduleId") Long scheduleId);

    @Query(value = "SELECT SUM(invited_users) " +
            "FROM booking b " +
            "inner join client c on c.id = b.client_id " +
            "inner join schedule_client sc on c.id = sc.client_id " +
            "inner join schedule s on s.id = sc.schedule_id " +
            "where sc.schedule_id = ?1", nativeQuery = true)
    Long findSumBookingClientByScheduleId(Long scheduleId);

    @Query(value = "SELECT * " +
            "FROM booking b " +
            "inner join client c on c.id = b.client_id " +
            "inner join schedule_client sc on c.id = sc.client_id " +
            "inner join schedule s on s.id = sc.schedule_id " +
            "where sc.schedule_id = ?1 and sc.client_id = ?2", nativeQuery = true)
    Optional<Booking> findBookingByClientAndByScheduleId(Long scheduleId, Long clientId);

    @Query(value = "select sum(b.invitedUsers) + sum(b.invitedChildren) " +
            "from Booking b join Schedule s on s.id = b.schedule.id " +
            "where b.isActive = true and s.route.id = ?1")
    Integer getCountBookedPlacesByRoutId(Long id);

    @Query(value = "select sum(b.invitedUsers) + sum(b.invitedChildren) " +
            "from Booking b join Schedule s on s.id = b.schedule.id " +
            "where b.isActive = true and s.activity.id = ?1 ")
    Integer getCountBookedPlacesByActivityId(Long id);


}
