package pl.pawlowski99.gym.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import pl.pawlowski99.gym.domain.Workout;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutRepository  extends CrudRepository<Workout, Long> {
    List<Workout> findByWorkoutStatus(@Param("workoutStatus")int status, Sort sort);
    List<Workout> findByUserUsernameAndWorkoutStatus(@Param("username")String username, @Param("workoutStatus")int status, Sort sort);
    List<Workout> findByUserUsername(@Param("username")String username, Sort sort);
    List<Workout> findByUserUsername(@Param("username")String username);
    List<Workout> findByWorkoutDateAndWorkoutStatus(@Param("workoutDate")LocalDate workoutDate, @Param("workoutStatus")int status);
}
