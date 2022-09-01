package pl.ug.Projekt.Zespolowy.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.ug.Projekt.Zespolowy.domain.Game;
import pl.ug.Projekt.Zespolowy.domain.Genre;
import pl.ug.Projekt.Zespolowy.domain.Rating;
import pl.ug.Projekt.Zespolowy.dto.GameDTO;
import pl.ug.Projekt.Zespolowy.repository.GenreRepository;
import pl.ug.Projekt.Zespolowy.service.GenreService;
import pl.ug.Projekt.Zespolowy.service.RatingService;
import pl.ug.Projekt.Zespolowy.utility.FileUploadUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class GenreController {

    private final GenreRepository repository;
    private final GenreService genreService;
    private final RatingService ratingService;

    GenreController(GenreRepository repository, GenreService genreService, RatingService ratingService){
        this.repository = repository;
        this.genreService = genreService;
        this.ratingService = ratingService;
    }

    @GetMapping("/genres")
    String getGenres(Model model){
        model.addAttribute("allGenres", repository.findAll());
        return "genre-list";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/genres/admin")
    String getGenresAdmin(Model model){
        model.addAttribute("allGenres", genreService.getAllGenresForAdminView());
        return "genre-list-admin";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/genre/save")
    public String genreForm(Model model) {
        model.addAttribute("Genre", new Genre());

        return "save-genre";
    }

    @GetMapping("/genre/{id}")
    String getGenre(@PathVariable Long id, Model model, Principal principal){
        String username = principal == null ? null : principal.getName();
        List<GameDTO> gameDTOS = repository.getById(id).getGames()
                .stream()
                .map(game -> mapToDto(game, username))
                .collect(Collectors.toList());

        model.addAttribute("allGames", gameDTOS);
        model.addAttribute("chosenGenre", repository.getById(id));
        return "genre-game-list";
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/genre/save")
    public String saveGenre(Model model, @ModelAttribute("Genre") Genre genre, @RequestParam("image") MultipartFile multipartFile) throws IOException {
        String filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        genre.setPathCover("/genres/" + filename);
        String uploadDir = "src/main/resources/static/genres";
        FileUploadUtil.saveFile(uploadDir, filename, multipartFile);
        repository.save(genre);

        return "redirect:/genres/admin";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/genre/edit/{id}")
    public String editGenre(@PathVariable("id") long id, ModelMap model) {
        model.addAttribute("editedGenre", repository.getById(id));

        return "edit-genre";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/genre/edit")
    public String editGenre(@ModelAttribute("editedGame") Genre genre){
        repository.save(genre);

        return "redirect:/genres/admin";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/genre/delete/{id}")
    public String deleteGenre(@PathVariable("id") long id) {
        repository.deleteById(id);

        return "redirect:/genres/admin";
    }
}
