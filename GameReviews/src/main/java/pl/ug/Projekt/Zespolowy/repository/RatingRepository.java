package pl.ug.Projekt.Zespolowy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ug.Projekt.Zespolowy.domain.Rating;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    List<Rating> findAllByGameIdOrderByPublicationDateDesc(Long gameId);


    List<Rating> findAllByGameId(Long gameId);

    List<Rating> findAllByUserUsername(String username);
    Rating findRatingByGameIdAndUserId(Long gameId, Long userId);

    Rating findRatingByGameIdAndUserUsername(Long gameId, String username);

    Integer countByGameId(Long gameId);

}
