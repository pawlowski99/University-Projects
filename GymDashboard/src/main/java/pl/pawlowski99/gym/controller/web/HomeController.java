package pl.pawlowski99.gym.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("")
    public String getHome() {
        return "home-page";
    }

    @GetMapping("/Calendar")
    public String getCalendar() {
        return "calendar";
    }
}
