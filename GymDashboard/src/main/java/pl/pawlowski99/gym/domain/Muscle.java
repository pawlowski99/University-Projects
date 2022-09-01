package pl.pawlowski99.gym.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
public class Muscle {

    @Id
    @GeneratedValue
    @Column(name="id")
    private Long id;

    @NotNull
    @Size(min=2, max=100)
    private String name;
    @JsonIgnore
    @OneToMany(orphanRemoval = true, mappedBy = "muscle", cascade = CascadeType.ALL)
    private List<Guide> guides;


    public Muscle() {
    }

    public Muscle(String name) {
        this.name = name;
    }

    public Muscle(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Guide> getGuides() {
        return guides;
    }

    public void setGuides(List<Guide> guides) {
        this.guides = guides;
    }

    @Override
    public String toString() {
        return "Muscle{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
