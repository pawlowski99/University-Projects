package pl.pawlowski99.gym.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;


@Entity
public class User {

    @Id
    @GeneratedValue
    @Column(name="id")
    private Long id;
    @Column(unique = true)
    @Size(min=4, max=100)
    private String username;
    @NotNull
    @Email
    @Size(min=6, max=100)
    private String email;
    @NotNull
    @Size(min=8, max=100)
    private String password;
    private LocalDate registerDate;
    private String firstName;
    private String lastName;
    private Role role;

    @OneToMany(orphanRemoval = true, mappedBy = "user", cascade = CascadeType.ALL)
    private List<Workout> workouts;

    public User() {
    }

    public User(String username, String email, String password, LocalDate registerDate, String firstName, String lastName) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.registerDate = registerDate;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(String username, String email, String password, LocalDate registerDate) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.registerDate = registerDate;
    }

    public User(String username, String email, String password, LocalDate registerDate, String firstName, String lastName, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.registerDate = registerDate;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public User(Long id, String username, String email, String password, LocalDate registerDate, String firstName, String lastName, Role role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.registerDate = registerDate;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public User(String username, String email, String password, LocalDate registerDate, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.registerDate = registerDate;
        this.role = role;
    }

    public User(Long id, String username, String email, String password, LocalDate registerDate, Role role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.registerDate = registerDate;
        this.role = role;
    }




    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getRegisterDate() {
        return registerDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRegisterDate(LocalDate registerDate) {
        this.registerDate = registerDate;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Workout> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(List<Workout> workouts) {
        this.workouts = workouts;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", registerDate=" + registerDate +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
