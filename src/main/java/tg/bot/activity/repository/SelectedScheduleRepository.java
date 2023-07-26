package tg.bot.activity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tg.bot.activity.model.entity.SelectedSchedule;

import java.util.Optional;

@Repository
public interface SelectedScheduleRepository extends JpaRepository<SelectedSchedule, Long> {

    Optional<SelectedSchedule> findByTelegramId(Long telegramId);

    void deleteByTelegramId(Long telegramId);
}
