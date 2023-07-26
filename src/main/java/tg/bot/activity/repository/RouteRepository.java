package tg.bot.activity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tg.bot.activity.model.entity.Route;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
}
