package pl.pawlowski99.gym.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.pawlowski99.gym.domain.Exercise;
import pl.pawlowski99.gym.domain.Workout;

import java.util.List;

@Repository
public interface ExerciseRepository extends CrudRepository<Exercise, Long> {
    List<Exercise> findByWorkoutUserUsername(@Param("username")String username);
}