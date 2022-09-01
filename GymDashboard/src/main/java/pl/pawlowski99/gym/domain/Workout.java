package pl.pawlowski99.gym.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Workout {

    @Id
    @GeneratedValue
    @Column(name="id")
    private Long id;

    @NotNull
    @Size(min=2, max=100)
    private String name;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate workoutDate;

    @NotNull
    @Range(min = 0, max = 1)
    private int workoutStatus;

    @OneToMany(orphanRemoval = true, mappedBy = "workout", cascade = CascadeType.ALL)
    private List<Exercise> exercises;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    public Workout() {
    }

    public Workout(String name, LocalDate workoutDate) {
        this.name = name;
        this.workoutDate = workoutDate;
    }

    public Workout(Long id, String name, LocalDate workoutDate) {
        this.id = id;
        this.name = name;
        this.workoutDate = workoutDate;
    }

    public Workout(Long id, String name, LocalDate workoutDate, int workoutStatus) {
        this.id = id;
        this.name = name;
        this.workoutDate = workoutDate;
        this.workoutStatus = workoutStatus;
    }

    public Workout(String name, LocalDate workoutDate, int workoutStatus) {
        this.name = name;
        this.workoutDate = workoutDate;
        this.workoutStatus = workoutStatus;
    }

    public Workout(String name, LocalDate workoutDate, int workoutStatus, User user) {
        this.name = name;
        this.workoutDate = workoutDate;
        this.workoutStatus = workoutStatus;
        this.user = user;
    }

    public Workout(Long id, String name, LocalDate workoutDate, int workoutStatus, User user) {
        this.id = id;
        this.name = name;
        this.workoutDate = workoutDate;
        this.workoutStatus = workoutStatus;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getWorkoutDate() {
        return workoutDate;
    }

    public int getWorkoutStatus() {
        return workoutStatus;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWorkoutDate(LocalDate workoutDate) {
        this.workoutDate = workoutDate;
    }

    public void setWorkoutStatus(int workoutStatus) {
        this.workoutStatus = workoutStatus;
    }

    public int getYear(){
        return workoutDate.getYear();
    }

    public int getMonth(){
        return workoutDate.getMonthValue();
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCompleted(){
        this.workoutStatus = 0;
    }

    public void setPlanned(){
        this.workoutStatus = 1;
    }

    public String getMonthAndYear(){
        return String.valueOf(this.workoutDate.getMonthValue())+"-"+String.valueOf(this.workoutDate.getYear());
    }


    @Override
    public String toString() {
        return "Workout{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", workoutDate=" + workoutDate +
                ", workoutStatus=" + workoutStatus +
                '}';
    }
}
