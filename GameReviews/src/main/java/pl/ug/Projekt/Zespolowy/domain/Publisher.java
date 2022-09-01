package pl.ug.Projekt.Zespolowy.domain;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "publisher_table")
public class Publisher {

    private @Id @GeneratedValue @Column(name="id") long ID;
    private String name;
    private String pathCover;

    @OneToMany(mappedBy = "publisher")
    private List<Game> games;

    public Publisher() {}
    public Publisher(String name, String pathCover) {
        this.name = name;
        this.pathCover = pathCover;
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

    public String getPathCover() {
        return pathCover;
    }

    public void setPathCover(String pathCover) {
        this.pathCover = pathCover;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    @Override
    public String toString() {
        return "Publisher{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", pathCover='" + pathCover + '\'' +
                '}';
    }
}
