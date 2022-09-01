package pl.ug.Projekt.Zespolowy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ug.Projekt.Zespolowy.domain.Publisher;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    Publisher findByName(String name);
}
