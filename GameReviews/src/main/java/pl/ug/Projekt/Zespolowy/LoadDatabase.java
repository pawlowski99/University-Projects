package pl.ug.Projekt.Zespolowy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.ug.Projekt.Zespolowy.domain.*;
import pl.ug.Projekt.Zespolowy.repository.GameRepository;
import pl.ug.Projekt.Zespolowy.repository.GenreRepository;
import pl.ug.Projekt.Zespolowy.repository.PublisherRepository;
import pl.ug.Projekt.Zespolowy.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initAdmin(UserRepository repository){
        return args ->{
            log.info("Preloading " + repository.save(new User("admin", new BCryptPasswordEncoder().encode("admin"), "admin", LocalDateTime.now(), Role.ADMIN)));
        };
    }
    @Bean
    CommandLineRunner initDatabase(GenreRepository genreRepository, PublisherRepository publisherRepository, GameRepository gameRepository){
        return args ->{
            initGenres(genreRepository);
            initPublishers(publisherRepository);

            Genre rpg = genreRepository.findByName("RPG");
            Publisher CDProjekt = publisherRepository.findByName("CDProjekt");

            log.info("Preloading" + gameRepository.save(new Game("Witcher 1", LocalDate.of(2015, 5, 19),
                    "You kill more monsters", "/covers/cover_witcher3.jpg", rpg, CDProjekt)));
            log.info("Preloading" + gameRepository.save(new Game("Witcher 2", LocalDate.of(2015, 5, 19),
                    "You kill more monsters", "/covers/cover_witcher1.jpg", rpg, CDProjekt)));
            log.info("Preloading" + gameRepository.save(new Game("Witcher 3", LocalDate.of(2015, 5, 19),
                    "You kill more monsters", "/covers/cover_witcher3.jpg", rpg, CDProjekt)));
        };
    }

    private void initGenres(GenreRepository repository) {
        log.info("Preloading " + repository.save(new Genre("RPG", "/genres/crossed-swords.png")));
        log.info("Preloading " + repository.save(new Genre("Racing", "/genres/checkered-flag.png")));
        log.info("Preloading " + repository.save(new Genre("Farming", "/genres/farmer.png")));
        log.info("Preloading " + repository.save(new Genre("Platformer", "/genres/flat-platform.png")));
        log.info("Preloading " + repository.save(new Genre("Rhythm-based", "/genres/musical-notes.png")));
        log.info("Preloading " + repository.save(new Genre("FPS", "/genres/pistol-gun.png")));
        log.info("Preloading " + repository.save(new Genre("Metroid-vania", "/genres/samus-helmet.png")));
        log.info("Preloading " + repository.save(new Genre("Action", "/genres/swordman.png")));
        log.info("Preloading " + repository.save(new Genre("RTS", "/genres/tower-flag.png")));
        log.info("Preloading " + repository.save(new Genre("Boxing", "/genres/boxing-glove.png")));
        log.info("Preloading " + repository.save(new Genre("Table-top", "/genres/card-ace-diamonds.png")));
        log.info("Preloading " + repository.save(new Genre("Turn-based", "/genres/extra-time.png")));
        log.info("Preloading " + repository.save(new Genre("Rouge-like", "/genres/hood.png")));
        log.info("Preloading " + repository.save(new Genre("Puzzle", "/genres/puzzle.png")));
        log.info("Preloading " + repository.save(new Genre("Adventure", "/genres/backpack.png")));
        log.info("Preloading " + repository.save(new Genre("VR", "/genres/vr-headset.png")));
    }

    private void initPublishers(PublisherRepository publisherRepository) {
        log.info("Preloading " + publisherRepository.save(new Publisher("Sony Interactive Entertainment", "/publishers/Sony_Interactive_Entertainment.png")));
        log.info("Preloading " + publisherRepository.save(new Publisher("Tencent Games", "/publishers/Tencent.png")));
        log.info("Preloading " + publisherRepository.save(new Publisher("Nintendo", "/publishers/Nintendo.png")));
        log.info("Preloading " + publisherRepository.save(new Publisher("Microsoft", "/publishers/Microsoft.png")));
        log.info("Preloading " + publisherRepository.save(new Publisher("NetEase", "/publishers/Netease.png")));
        log.info("Preloading " + publisherRepository.save(new Publisher("Activision Blizzard", "/publishers/Activision_Blizzard.png")));
        log.info("Preloading " + publisherRepository.save(new Publisher("Electronic Arts", "/publishers/Electronic_Arts.png")));
        log.info("Preloading " + publisherRepository.save(new Publisher("Take-Two Interactive", "/publishers/Take-Two_Interactive.png")));
        log.info("Preloading " + publisherRepository.save(new Publisher("Square Enix", "/publishers/Square_Enix.png")));
        log.info("Preloading " + publisherRepository.save(new Publisher("Nexon", "/publishers/Nexon.png")));
        log.info("Preloading " + publisherRepository.save(new Publisher("Ubisoft", "/publishers/Ubisoft.png")));
        log.info("Preloading " + publisherRepository.save(new Publisher("Konami", "/publishers/Konami.png")));
        log.info("Preloading " + publisherRepository.save(new Publisher("Sega", "/publishers/SEGA.png")));
        log.info("Preloading " + publisherRepository.save(new Publisher("Capcom", "/publishers/Capcom.png")));
        log.info("Preloading " + publisherRepository.save(new Publisher("Bandai Namco", "/publishers/Bandai_Namco.png")));
        log.info("Preloading " + publisherRepository.save(new Publisher("CDProjekt", "/publishers/cd_projekt_logo.png")));
    }

    /*
        @Bean
    CommandLineRunner initDatabaseGame(GameRepository gameRepository){
        return args ->{
            // String nameGame, String dateRelease, String description, String linkCover, int idGenre, int idPublisher
            log.info("Preloading " + gameRepository.save(new Game("Witcher", "26.10.2007" ,"You kill monsters", "", 1,1 )));
            log.info("Preloading " + gameRepository.save(new Game("Witcher 2: Assassins of Kings", "10.05.2011" ,"You kill more monsters", "", 1,1 )));
            log.info("Preloading " + gameRepository.save(new Game("Witcher 3: Wild Hunt", "19.05.2015" ,"You kill even more monsters", "", 1,1 )));
            log.info("Preloading " + gameRepository.save(new Game("Doom", "12.05.2016" ,"You kill demons", "", 2,2 )));
            log.info("Preloading " + gameRepository.save(new Game("Doom Eternal", "20.04.2020" ,"You kill more demons", "", 2,2 )));
            log.info("Preloading " + gameRepository.save(new Game("The Elder Scrolls V: Skyrim", "11.11.2011" ,"You kill dragons", "", 1,2 )));
        };
    }
     */

}