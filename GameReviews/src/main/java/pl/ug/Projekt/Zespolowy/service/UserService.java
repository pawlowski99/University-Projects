package pl.ug.Projekt.Zespolowy.service;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.ug.Projekt.Zespolowy.domain.Role;
import pl.ug.Projekt.Zespolowy.domain.User;
import pl.ug.Projekt.Zespolowy.dto.UserRequestDTO;
import pl.ug.Projekt.Zespolowy.repository.UserRepository;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void register(UserRequestDTO userRequestDTO) {
        User newUser = buildNewUser(userRequestDTO);
        userRepository.save(newUser);

    }

    private User buildNewUser(UserRequestDTO userRequestDTO) {
        User newUser = new User();
        newUser.setUsername(userRequestDTO.getUsername());
        newUser.setEncodedPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        newUser.setEmail(userRequestDTO.getEmail());
        newUser.setRegistrationDate(LocalDateTime.now());
        newUser.setRole(Role.USER);
        return newUser;
    }
}
