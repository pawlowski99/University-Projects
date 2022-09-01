package pl.pawlowski99.gym.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Range;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

@Entity
public class Exercise {

    @Id
    @GeneratedValue
    @Column(name="id")
    private Long id;

    @NotNull
    @Digits(integer = 3, fraction = 0)
    @Range(min = 1, max = 300)
    private int sets;

    @NotNull
    @Digits(integer = 3, fraction = 0)
    @Range(min = 1, max = 999)
    private int reps;

    @Digits(integer = 3, fraction = 0)
    @Range(min = 0, max = 999)
    private int weights = 0;

    @ManyToOne
    @JoinColumn(name="workout_id")
    private Workout workout;

    @ManyToOne
    @JoinColumn(name="guide_id")
    private Guide guide;

    public Exercise() {
    }

    public Exercise(int sets, int reps, int weights) {
        this.sets = sets;
        this.reps = reps;
        this.weights = weights;
    }

    public Exercise(Long id, int sets, int reps, int weights) {
        this.id = id;
        this.sets = sets;
        this.reps = reps;
        this.weights = weights;
    }

    public Exercise(Long id, int sets, int reps) {
        this.id = id;
        this.sets = sets;
        this.reps = reps;
    }

    public Exercise(int sets, int reps) {
        this.sets = sets;
        this.reps = reps;
    }

    public Exercise(int sets, int reps, int weights, Workout workout, Guide guide) {
        this.sets = sets;
        this.reps = reps;
        this.weights = weights;
        this.workout = workout;
        this.guide = guide;
    }

    public Exercise(int sets, int reps, int weights, Guide guide) {
        this.sets = sets;
        this.reps = reps;
        this.weights = weights;
        this.guide = guide;
    }

    public Exercise(int sets, int reps, int weights, Workout workout) {
        this.sets = sets;
        this.reps = reps;
        this.weights = weights;
        this.workout = workout;
    }

    public Exercise(Long id, int sets, int reps, int weights, Workout workout, Guide guide) {
        this.id = id;
        this.sets = sets;
        this.reps = reps;
        this.weights = weights;
        this.workout = workout;
        this.guide = guide;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getWeights() {
        return weights;
    }

    public void setWeights(int weights) {
        this.weights = weights;
    }

    public Workout getWorkout() {
        return workout;
    }

    public void setWorkout(Workout workout) {
        this.workout = workout;
    }

    public Guide getGuide() {
        return guide;
    }

    public void setGuide(Guide guide) {
        this.guide = guide;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "id=" + id +
                ", sets=" + sets +
                ", reps=" + reps +
                ", weights=" + weights +
                '}';
    }
}
