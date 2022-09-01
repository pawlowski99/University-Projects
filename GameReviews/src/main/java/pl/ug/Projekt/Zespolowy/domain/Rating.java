package pl.ug.Projekt.Zespolowy.domain;

import pl.ug.Projekt.Zespolowy.repository.RatingRepository;
import pl.ug.Projekt.Zespolowy.service.RatingService;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"user_id", "game_id"})
})
public class Rating {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User user;

    @OneToOne
    @JoinColumn(name="game_id", referencedColumnName = "id")
    private Game game;

    @Min(1)
    @Max(10)
    private Integer value = 1;

    @Size(max = 255)
    private String comment;

    private LocalDateTime publicationDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDateTime publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String FormattedDateToString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return publicationDate.format(formatter);
    }

    public Rating() {
    }
}
