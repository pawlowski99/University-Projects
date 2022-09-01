package pl.ug.Projekt.Zespolowy.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.ug.Projekt.Zespolowy.domain.Game;

import java.time.LocalDate;
import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {

    List<Game> findByDateReleaseGreaterThanEqual(LocalDate time, Sort sort);
}
