package pl.ug.Projekt.Zespolowy.service;

import org.springframework.stereotype.Service;
import pl.ug.Projekt.Zespolowy.domain.Rating;
import pl.ug.Projekt.Zespolowy.repository.RatingRepository;

import java.util.List;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public Rating getById(Long id) {
        return ratingRepository.getById(id);
    }

    public List<Rating> findRatingsByGameId(Long gameId) {
        return ratingRepository.findAllByGameIdOrderByPublicationDateDesc(gameId);
    }

    public List<Rating> findAll(){
        return ratingRepository.findAll();
    }

    public List<Rating> findAllByUserUsername(String username){
        return ratingRepository.findAllByUserUsername(username);
    }

    public boolean isUserRatedGame(Long gameId, String username) {
        return ratingRepository.findRatingByGameIdAndUserUsername(gameId, username) != null;
    }

    public Rating getRating(Long gameId, String username) {
        return ratingRepository.findRatingByGameIdAndUserUsername(gameId, username);
    }

    public Double getAverageValue(Long gameId) {
        return ratingRepository.findAllByGameId(gameId)
                .stream()
                .mapToInt(Rating::getValue)
                .average()
                .orElse(0);
    }

    public Rating save(Rating rating) {
        return ratingRepository.save(rating);
    }

    public Integer getNumberOfVotes(Long gameId) {
        return ratingRepository.countByGameId(gameId);
    }
}
