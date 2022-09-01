package pl.pawlowski99.gym.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.pawlowski99.gym.domain.Exercise;
import pl.pawlowski99.gym.domain.User;
import pl.pawlowski99.gym.domain.Workout;
import pl.pawlowski99.gym.service.ExerciseService;
import pl.pawlowski99.gym.service.WorkoutService;
import org.springframework.stereotype.Controller;
import pl.pawlowski99.gym.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDate;

@Controller
public class AuthenticationController {
    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String getRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping(value = "/register")
    public String register(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()){
            return "register";
        }

        if(userService.getUserByUsername(user.getUsername()).isPresent()){
            model.addAttribute("usernameTaken", 1);
            return "register";
        }

        userService.registerUser(user);
        return "register-success";
    }

    @GetMapping(value="/logout")
    public String logout(HttpServletRequest request){
        HttpSession httpSession = request.getSession();
        httpSession.invalidate();
        return "redirect:/";
    }


}
