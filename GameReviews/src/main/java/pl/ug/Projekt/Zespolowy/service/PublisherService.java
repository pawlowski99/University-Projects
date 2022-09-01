package pl.ug.Projekt.Zespolowy.service;

import org.springframework.stereotype.Service;
import pl.ug.Projekt.Zespolowy.domain.Publisher;
import pl.ug.Projekt.Zespolowy.dto.PublisherDTO;
import pl.ug.Projekt.Zespolowy.repository.PublisherRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublisherService {

    private final PublisherRepository publisherRepository;

    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public List<PublisherDTO> getAllPublishersForAdminView() {
        return publisherRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private PublisherDTO mapToDto(Publisher publisher) {
        PublisherDTO publisherDTO = new PublisherDTO();
        publisherDTO.setID(publisher.getID());
        publisherDTO.setPathCover(publisher.getPathCover());
        publisherDTO.setName(publisher.getName());
        publisherDTO.setCanBeDeleted(canGenreBeDeleted(publisher));

        return publisherDTO;
    }

    private Boolean canGenreBeDeleted(Publisher publisher) {
        return publisher.getGames().isEmpty();
    }
}
