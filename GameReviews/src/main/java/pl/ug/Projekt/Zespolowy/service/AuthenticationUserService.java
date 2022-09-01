package pl.ug.Projekt.Zespolowy.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.ug.Projekt.Zespolowy.domain.User;

import java.util.Collections;

@Service
public class AuthenticationUserService implements UserDetailsService {

    private final UserService userService;

    public AuthenticationUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User dbUser = userService.findByUsername(username);
        if (dbUser == null) {
            throw new UsernameNotFoundException("User not found - username - " + username);
        }
        UserDetails userDetails = new org.springframework.security.core.userdetails
                .User(dbUser.getUsername(),
                dbUser.getEncodedPassword(),
                Collections.singleton(new SimpleGrantedAuthority(dbUser.getRole().getName())));


        return userDetails;
    }
}
