package pl.ug.Projekt.Zespolowy.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;


@Entity
@Table(name="game_table")
public class Game {

    @Id
    @GeneratedValue
    @Column(name="id")
    private Long id;

    private String nameGame;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateRelease;
    private String description;
    private String pathCover;

    @ManyToOne
    @JoinColumn(name="genre_id", referencedColumnName = "id")
    private Genre genre;

    @ManyToOne
    @JoinColumn(name = "publisher_id", referencedColumnName = "id")
    private Publisher publisher;


    // @ManyToMany()
    //private List<Console> consoles = new ArrayList<>();

    public Game() {

    }

    public Game(String nameGame) {
        this.nameGame = nameGame;
    }

    public Game(String nameGame, LocalDate dateRelease, String description, String pathCover, Genre genre, Publisher publisher) {
        this.nameGame = nameGame;
        this.dateRelease = dateRelease;
        this.description = description;
        this.pathCover = pathCover;
        this.genre = genre;
        this.publisher = publisher;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long ID) {
        this.id = ID;
    }

    public String getNameGame() {
        return nameGame;
    }

    public void setNameGame(String nameGame) {
        this.nameGame = nameGame;
    }

    public LocalDate getDateRelease() {
        return dateRelease;
    }

    public void setDateRelease(LocalDate dateRelease) {
        this.dateRelease = dateRelease;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPathCover() {
        return pathCover;
    }

    public void setPathCover(String idCover) {
        this.pathCover = idCover;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    /*
    public List<Console> getConsoles() {
        return consoles;
    }
    public void setConsoles(List<Console> consoles) {
        this.consoles = consoles;
    }
     */

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "Game{" + "id=" + this.id + ", name='" + this.nameGame + '}';
    }
}