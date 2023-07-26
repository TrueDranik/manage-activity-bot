package tg.bot.activity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tg.bot.activity.model.entity.Instructor;

import java.util.Optional;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {

    Optional<Instructor> findByTelegramId(Long telegramId);

    void deleteByTelegramId(Long telegramId);

    boolean existsByTelegramId(Long telegramId);
}

