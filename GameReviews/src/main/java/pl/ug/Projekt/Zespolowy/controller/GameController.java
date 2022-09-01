package pl.ug.Projekt.Zespolowy.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.ug.Projekt.Zespolowy.domain.Game;
import pl.ug.Projekt.Zespolowy.dto.GameDTO;
import pl.ug.Projekt.Zespolowy.repository.ConsoleRepository;
import pl.ug.Projekt.Zespolowy.repository.GameRepository;
import pl.ug.Projekt.Zespolowy.repository.GenreRepository;
import pl.ug.Projekt.Zespolowy.repository.PublisherRepository;
import pl.ug.Projekt.Zespolowy.service.GameService;
import pl.ug.Projekt.Zespolowy.service.RatingService;
import pl.ug.Projekt.Zespolowy.utility.FileUploadUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class GameController {

    private final GameRepository gameRepository;
    private final GenreRepository genreRepository;
    private final PublisherRepository publisherRepository;
    private final ConsoleRepository consoleRepository;
    private final RatingService ratingService;
    private final GameService gameService;

    GameController(GameRepository repository, GenreRepository genreRepository, PublisherRepository publisherRepository,
                   ConsoleRepository consoleRepository, RatingService ratingService, GameService gameService){
        this.gameRepository = repository;
        this.genreRepository = genreRepository;
        this.publisherRepository = publisherRepository;
        this.consoleRepository = consoleRepository;
        this.ratingService = ratingService;
        this.gameService = gameService;
    }

    @GetMapping("/games")
    String getGame(Model model, Principal principal){

        String username = principal == null ? null : principal.getName();

        List<GameDTO> gameDTOS = gameRepository.findAll()
                .stream()
                .map(game -> mapToDto(game, username))
                .collect(Collectors.toList());
        
        model.addAttribute("allGames", gameDTOS);
        return "game-list";
    }

    @GetMapping("/games/new")
    String getNewGame(Model model, Principal principal){

        String username = principal == null ? null : principal.getName();

        List<GameDTO> gameDTOS = gameService.getGamesReleasedLast7Days()
                .stream()
                .map(game -> mapToDto(game, username))
                .collect(Collectors.toList());

        model.addAttribute("allGames", gameDTOS);
        return "game-list-new";
    }

    @GetMapping("/game/{id}")
    String getGame(@PathVariable Long id, Model model, Principal principal){

        String username = principal == null ? null : principal.getName();

        model.addAttribute("game", mapToDto(gameRepository.getById(id), username));
        model.addAttribute("isRated", ratingService.isUserRatedGame(id, username));
        model.addAttribute("ratings", ratingService.findRatingsByGameId(id));
        model.addAttribute("viewer", username);

        return "game";
    }


    @GetMapping("/games/sorted")
    String getGameSorted(Model model, Principal principal){

        String username = principal == null ? null : principal.getName();

        List<GameDTO> gameDTOS = gameRepository.findAll()
                .stream()
                .map(game -> mapToDto(game, username))
                .sorted((o1, o2) -> o2.getAverageValue().compareTo(o1.getAverageValue()))
                .collect(Collectors.toList());

        model.addAttribute("allGames", gameDTOS);
        return "ranking-list";
    }

    @GetMapping("/games/sorted/chose")
    String getGameSortedMode(Model model, Principal principal, @RequestParam("mode") String mode){
        String username = principal == null ? null : principal.getName();
        if(mode.equals("Descending")){
            List<GameDTO> gameDTOS = gameRepository.findAll()
                    .stream()
                    .map(game -> mapToDto(game, username))
                    .sorted((o1, o2) -> o2.getAverageValue().compareTo(o1.getAverageValue()))
                    .collect(Collectors.toList());

            model.addAttribute("allGames", gameDTOS);
            model.addAttribute("order", 0);
        }
        else if(mode.equals("Ascending")){
            List<GameDTO> gameDTOS = gameRepository.findAll()
                    .stream()
                    .map(game -> mapToDto(game, username))
                    .sorted((o1, o2) -> o1.getAverageValue().compareTo(o2.getAverageValue()))
                    .collect(Collectors.toList());

            model.addAttribute("allGames", gameDTOS);
            model.addAttribute("order", 1);
        }

        return "ranking-list";

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/games/admin")
    String getGameAdmin(Model model){
        model.addAttribute("allGames", gameRepository.findAll());
        return "game-list-admin";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/game/save")
    public String addGame(ModelMap model) {
        model.addAttribute("newGame", new Game());
        model.addAttribute("genres", genreRepository.findAll());
        model.addAttribute("publishers", publisherRepository.findAll());
        return "save-game";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/game/save")
    public String saveGame(@ModelAttribute("saveGame") Game game, ModelMap model, @RequestParam("image") MultipartFile multipartFile) throws IOException {
        String filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        game.setPathCover("/covers/"+filename);
        String uploadDir = "src/main/resources/static/covers";
        FileUploadUtil.saveFile(uploadDir, filename, multipartFile);
        gameRepository.save(game);

        model.addAttribute("allGames", gameRepository.findAll());
        return "redirect:/games/admin/";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/game/edit/{id}")
    public String editGame(@PathVariable("id") long id, ModelMap model) {
        model.addAttribute("editedGame", gameRepository.getById(id));
        model.addAttribute("genres", genreRepository.findAll());
        model.addAttribute("publishers", publisherRepository.findAll());
        return "edit-game";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/game/edit")
    public String editGame(@ModelAttribute("editedGame") Game game, ModelMap model){
        gameRepository.save(game);
        //model.addAttribute("allGames", gameRepository.findAll());
        return "redirect:/games/admin/";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/game/delete/{id}")
    public String deleteGame(@PathVariable("id") long id, ModelMap model) {
        gameRepository.deleteById(id);
        model.addAttribute("allGames", gameRepository.findAll());
        return "redirect:/games/admin/";
    }

    private GameDTO mapToDto(Game game, String username) {
        GameDTO gameDTO = new GameDTO();
        BigDecimal averageValue = new BigDecimal(ratingService.getAverageValue(game.getId())).setScale(1, RoundingMode.HALF_EVEN);

        gameDTO.setId(game.getId());
        gameDTO.setNameGame(game.getNameGame());
        gameDTO.setPathCover(game.getPathCover());
        gameDTO.setDescription(game.getDescription());
        gameDTO.setGenre(game.getGenre());
        gameDTO.setPublisher(game.getPublisher());
        gameDTO.setDateRelease(game.getDateRelease());
        gameDTO.setAverageValue(averageValue.doubleValue());
        gameDTO.setStarRating(averageValue.intValue());
        gameDTO.setVotes(ratingService.getNumberOfVotes(game.getId()));
        gameDTO.setIsRated(ratingService.isUserRatedGame(game.getId(), username));

        return gameDTO;
    }

}

