package pl.pawlowski99.gym.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pawlowski99.gym.domain.Guide;

@Repository
public interface GuideRepository extends CrudRepository<Guide, Long> {
}
