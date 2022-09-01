package pl.pawlowski99.gym.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
public class Guide {

    @Id
    @GeneratedValue
    @Column(name="id")
    private Long id;

    @NotNull
    @Size(min=2, max=100)
    private String name;

    @NotNull
    @Size(min=2)
    private String description;

    @ManyToOne
    @JoinColumn(name="muscle_id")
    private Muscle muscle;

    @OneToMany(orphanRemoval = true, mappedBy = "guide", cascade = CascadeType.ALL)
    private List<Exercise> exercises;

    public Guide() {
    }

    public Guide(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Guide(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Muscle getMuscle() {
        return muscle;
    }

    public void setMuscle(Muscle muscle) {
        this.muscle = muscle;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public List<String> getSplitDescription(){
        List<String> list = new ArrayList<String>(Arrays.asList(this.description.split("\n")));
        return list;
    }

    public String getHead(){
        String noNL = this.description.replace("\n", " ");
        Integer maxLen = noNL.length();
        Integer a = 30;
        String subStr;

        if(maxLen<=a){
            subStr = noNL.substring(0) ;
        }

        else{
            subStr = noNL.substring(0, a)+ " ...";
        }
        return subStr;
    }

    @Override
    public String toString() {
        return "Guide{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
