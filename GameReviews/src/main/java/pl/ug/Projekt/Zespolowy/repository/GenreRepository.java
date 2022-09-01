package pl.ug.Projekt.Zespolowy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ug.Projekt.Zespolowy.domain.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    Genre findByName(String name);
}
