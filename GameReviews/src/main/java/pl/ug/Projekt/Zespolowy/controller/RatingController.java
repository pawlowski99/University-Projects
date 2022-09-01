package pl.ug.Projekt.Zespolowy.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.ug.Projekt.Zespolowy.domain.Game;
import pl.ug.Projekt.Zespolowy.dto.GameDTO;
import pl.ug.Projekt.Zespolowy.domain.Rating;
import pl.ug.Projekt.Zespolowy.domain.User;
import pl.ug.Projekt.Zespolowy.service.GameService;
import pl.ug.Projekt.Zespolowy.service.RatingService;
import pl.ug.Projekt.Zespolowy.service.UserService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/rating")
public class RatingController {

    private final RatingService ratingService;
    private final GameService gameService;
    private final UserService userService;

    public RatingController(RatingService ratingService, GameService gameService, UserService userService) {
        this.ratingService = ratingService;
        this.gameService = gameService;
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/save/{gameId}")
    String getCreateRatingForm(@PathVariable Long gameId, Model model){

        Rating rating = new Rating();
        Game game = gameService.getById(gameId);

        model.addAttribute("rating", rating);
        model.addAttribute("game", game);

        return "save-rating";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/myreviews")
    String getMyReviews(Principal principal, Model model){
        String username = principal == null ? null : principal.getName();
        List<Rating> ratingList = ratingService.findAllByUserUsername(username);
        model.addAttribute("allRatings", ratingList);

        return "reviews-list";
    }
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/edit/{gameId}")
    String getEditRatingForm(@PathVariable Long gameId, Model model, Principal principal){

        Rating rating = ratingService.getRating(gameId, principal.getName());
        Game game = gameService.getById(gameId);

        model.addAttribute("rating", rating);
        model.addAttribute("game", game);

        return "edit-rating";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/{gameId}")
    String addRating(@PathVariable Long gameId, @ModelAttribute("rating") Rating rating, Model model, Principal principal) {

        User user = userService.findByUsername(principal.getName());
        Game game = gameService.getById(gameId);

        rating.setGame(game);
        rating.setPublicationDate(LocalDateTime.now());
        rating.setUser(user);

        ratingService.save(rating);

        String username = principal == null ? null : principal.getName();
        model.addAttribute("isRated", ratingService.isUserRatedGame(gameId, username));
        model.addAttribute("game", mapToDto(game, username));
        model.addAttribute("ratings", ratingService.findRatingsByGameId(game.getId()));
        model.addAttribute("viewer", username);

        return "game";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/edit/{gameId}")
    String editRanking(@PathVariable Long gameId, @ModelAttribute("rating") Rating rating, Model model, Principal principal) {

        Game game = gameService.getById(gameId);
        Rating dbRating = ratingService.getRating(gameId, principal.getName());

        dbRating.setComment(rating.getComment());
        dbRating.setValue(rating.getValue());

        ratingService.save(rating);

        String username = principal == null ? null : principal.getName();
        model.addAttribute("isRated", ratingService.isUserRatedGame(gameId, username));
        model.addAttribute("game", mapToDto(game, username));
        model.addAttribute("ratings", ratingService.findRatingsByGameId(game.getId()));
        model.addAttribute("viewer", username);
        
        return "game";
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
