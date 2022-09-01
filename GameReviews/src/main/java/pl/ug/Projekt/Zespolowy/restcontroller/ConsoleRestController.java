package pl.ug.Projekt.Zespolowy.restcontroller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.ug.Projekt.Zespolowy.domain.Console;
import pl.ug.Projekt.Zespolowy.domain.Game;
import pl.ug.Projekt.Zespolowy.repository.ConsoleRepository;
import pl.ug.Projekt.Zespolowy.repository.GameRepository;

import java.util.List;

@RestController
public class ConsoleRestController {

    private final ConsoleRepository repository;

    public ConsoleRestController(ConsoleRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/api/console")
    List<Console> getAllGames(){
        return repository.findAll();
    }
}
