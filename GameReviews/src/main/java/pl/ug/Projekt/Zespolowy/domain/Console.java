package pl.ug.Projekt.Zespolowy.domain;


import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Console {

    private @Id @GeneratedValue @Column(name="id") long ID;
    private String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateRelease;

    //@ManyToMany()
    //private List<Game> game;

    public Console(){}

    public Console(String name, LocalDate dateRelease) {
        this.name = name;
        this.dateRelease = dateRelease;

    }


    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateRelease() {
        return dateRelease;
    }

    public void setDateRelease(LocalDate dateRelease) {
        this.dateRelease = dateRelease;
    }

    /*
    public List<Game> getGame() {
        return game;
    }

    public void setGame(List<Game> game) {
        this.game = game;
    }


     */
    @Override
    public String toString() {
        return "Console{" +
                "name='" + name + '\'' +
                '}';
    }
}
