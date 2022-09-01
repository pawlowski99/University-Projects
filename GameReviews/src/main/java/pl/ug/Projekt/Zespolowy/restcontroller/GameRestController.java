package pl.ug.Projekt.Zespolowy.restcontroller;

import org.springframework.web.bind.annotation.*;
import pl.ug.Projekt.Zespolowy.domain.Game;
import pl.ug.Projekt.Zespolowy.repository.GameRepository;

import java.util.List;

@RestController

public class GameRestController {
    private final GameRepository repository;

    public GameRestController(GameRepository repository) {
        this.repository = repository;
    }

//    @GetMapping("/game")
//    List<Game> getAllGames(){
//        return repository.findAll();
//    }

    @DeleteMapping("/api/game/{id}")
    void deleteGame(@PathVariable Long id){
        repository.deleteById(id);
    }

    @PutMapping("/api/game/{id}")
    Game replaceGame(@RequestBody Game newGame, @PathVariable Long id){

        return repository.findById(id)
                .map(employee -> {
                    employee.setNameGame(newGame.getNameGame());
                    return repository.save(employee);
                })
                .orElseGet(() -> {
                    newGame.setId(id);
                    return repository.save(newGame);
                });
    }
}
