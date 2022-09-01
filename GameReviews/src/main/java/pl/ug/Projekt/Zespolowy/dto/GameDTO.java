package pl.ug.Projekt.Zespolowy.dto;

import pl.ug.Projekt.Zespolowy.domain.Genre;
import pl.ug.Projekt.Zespolowy.domain.Publisher;

import java.time.LocalDate;

public class GameDTO {

    private Long id;
    private String nameGame;
    private LocalDate dateRelease;
    private String description;
    private String pathCover;
    private Genre genre;
    private Publisher publisher;
    private Double averageValue;
    private Integer starRating;
    private Integer votes;
    private Boolean isRated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setPathCover(String pathCover) {
        this.pathCover = pathCover;
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

    public Double getAverageValue() {
        return averageValue;
    }

    public void setAverageValue(Double averageValue) {
        this.averageValue = averageValue;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public Boolean getIsRated() {
        return isRated;
    }

    public void setIsRated(Boolean rated) {
        isRated = rated;
    }

    public Integer getStarRating() { return starRating; }

    public void setStarRating(int starRating) { this.starRating = starRating; }
}
