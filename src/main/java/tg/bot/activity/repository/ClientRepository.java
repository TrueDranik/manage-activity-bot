package tg.bot.activity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tg.bot.activity.model.entity.Client;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("SELECT c FROM Client c JOIN Booking b on c.id = b.client.id where b.schedule.id = ?1")
    List<Client> findClientByScheduleId(Long id);

    boolean existsByTelegramId(Long chatId);

    Client findByPhoneNumber(String phoneNumber);

    Optional<Client> findByTelegramId(Long telegramId);
}