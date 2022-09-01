package pl.ug.Projekt.Zespolowy.service;

import org.springframework.stereotype.Service;
import pl.ug.Projekt.Zespolowy.domain.Genre;
import pl.ug.Projekt.Zespolowy.dto.GenreDTO;
import pl.ug.Projekt.Zespolowy.repository.GenreRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<GenreDTO> getAllGenresForAdminView() {
        return genreRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private GenreDTO mapToDto(Genre genre) {
        GenreDTO genreDTO = new GenreDTO();
        genreDTO.setID(genre.getID());
        genreDTO.setPathCover(genre.getPathCover());
        genreDTO.setName(genre.getName());
        genreDTO.setCanBeDeleted(canGenreBeDeleted(genre));

        return genreDTO;
    }

    private Boolean canGenreBeDeleted(Genre genre) {
        return genre.getGames().isEmpty();
    }
}
