package pl.ug.Projekt.Zespolowy.service;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.ug.Projekt.Zespolowy.domain.Game;
import pl.ug.Projekt.Zespolowy.repository.GameRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<Game> getGamesReleasedLast7Days() {
        Sort sort = Sort.by(Sort.Direction.DESC, "dateRelease");
        return gameRepository.findByDateReleaseGreaterThanEqual(LocalDate.now().minusDays(7), sort);
    }

    public Game getById(Long id) {
        return gameRepository.getById(id);
    }
}
