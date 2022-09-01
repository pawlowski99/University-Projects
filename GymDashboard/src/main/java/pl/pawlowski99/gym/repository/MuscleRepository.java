package pl.pawlowski99.gym.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pawlowski99.gym.domain.Muscle;

@Repository
public interface MuscleRepository extends CrudRepository<Muscle, Long> {
}
