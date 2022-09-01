package pl.ug.Projekt.Zespolowy.restcontroller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.ug.Projekt.Zespolowy.domain.Publisher;
import pl.ug.Projekt.Zespolowy.repository.PublisherRepository;

import java.util.List;

@RestController
public class PublisherRestController {


    private final PublisherRepository publisherRepository;

    public PublisherRestController(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    @GetMapping("/api/publishers")
    List<Publisher> all(){
        return publisherRepository.findAll();
    }
}
