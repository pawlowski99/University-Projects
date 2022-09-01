package pl.pawlowski99.gym.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.pawlowski99.gym.domain.Role;
import pl.pawlowski99.gym.domain.User;
import pl.pawlowski99.gym.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public User saveUser(User user, Long id){
        user.setId(id);
        return userRepository.save(user);
    }

    public Optional<User> getUserById (Long id){
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername (String username){
        return userRepository.findByUsername(username);
    }

    public Iterable<User> getAllUsers(){
        return userRepository.findAll();
    }

    public void deleteUserById(Long id){
        userRepository.deleteById(id);
    }

    public void registerUser(User user) {
        User newUser = new User();
        newUser.setId(user.getId());
        newUser.setUsername(user.getUsername());
        newUser.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        newUser.setEmail(user.getEmail());
        newUser.setRegisterDate(LocalDate.now());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setRole(Role.USER);
        userRepository.save(newUser);
    }

    public void editUser(User user) {
        User newUser = new User();
        newUser.setId(user.getId());
        newUser.setUsername(user.getUsername());
        newUser.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        newUser.setEmail(user.getEmail());
        newUser.setRegisterDate(LocalDate.now());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setRole(user.getRole());
        newUser.setWorkouts(user.getWorkouts());
        userRepository.save(newUser);
    }

    public void registerUserAdmin(User user) {

        User newUser = new User();
        newUser.setId(user.getId());
        newUser.setUsername(user.getUsername());
        newUser.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        newUser.setEmail(user.getEmail());
        newUser.setRegisterDate(LocalDate.now());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setRole(user.getRole());
        newUser.setWorkouts(user.getWorkouts());
        userRepository.save(newUser);

    }
}
