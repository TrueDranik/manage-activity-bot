package tg.bot.activity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tg.bot.activity.model.entity.AboutUs;

import java.util.Optional;

@Repository
public interface AboutUsRepository extends JpaRepository<AboutUs, Long> {

    @Query("SELECT au FROM AboutUs au")
    Optional<AboutUs> getAboutUs();
}
