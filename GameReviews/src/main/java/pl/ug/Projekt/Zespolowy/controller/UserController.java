package pl.ug.Projekt.Zespolowy.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.ug.Projekt.Zespolowy.domain.Rating;
import pl.ug.Projekt.Zespolowy.domain.User;
import pl.ug.Projekt.Zespolowy.repository.UserRepository;
import pl.ug.Projekt.Zespolowy.service.RatingService;
import pl.ug.Projekt.Zespolowy.utility.FileUploadUtil;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
public class UserController {

    private final UserRepository userRepository;
    private final RatingService ratingService;

    UserController(UserRepository userRepository, RatingService ratingService){
        this.userRepository = userRepository;
        this.ratingService = ratingService;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/web/user")
    String getUser(Model model, Principal principal) {
        model.addAttribute("viewedUser", userRepository.findByUsername(principal.getName()));
        List<Rating> ratingList = ratingService.findAllByUserUsername(principal.getName());
        if(!ratingList.isEmpty()) {
            model.addAttribute("lastReview", ratingList.get(ratingList.size() - 1).FormattedDateToString());
            model.addAttribute("reviewCount", ratingList.size());
        }
        else {
            model.addAttribute("lastReview", "No activity");
            model.addAttribute("reviewCount", 0);
        }

        return "user-profile";
    }

    @GetMapping("/web/users")
    String getUsers(Model model){
        model.addAttribute("allUsers", userRepository.findAll());

        return "user-list";
    }

    @GetMapping("/web/user/{id}")
    String getUser(@PathVariable("id") long id, Model model){
        model.addAttribute("viewedUser", userRepository.getById(id));
        List<Rating> ratingList = ratingService.findAllByUserUsername(userRepository.getById(id).getUsername());
        if(!ratingList.isEmpty()) {
            model.addAttribute("lastReview", ratingList.get(0).FormattedDateToString());
            model.addAttribute("reviewCount", ratingList.size());
        }
        else {
            model.addAttribute("lastReview", "No activity");
            model.addAttribute("reviewCount", 0);
        }

        return "user-profile";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value = "/web/user/edit")
    public String editGame(Model model, Principal principal) {
        model.addAttribute("viewedUser", userRepository.findByUsername(principal.getName()));

        return "edit-profile";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/web/user/edit")
    public String editGame(@ModelAttribute("viewedUser") User user, @RequestParam("image") MultipartFile multipartFile, Principal principal) throws IOException {

        User dbUser = userRepository.findByUsername(principal.getName());

        String filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        dbUser.setPathAvatar("/avatars/" + filename);
        String uploadDir = "src/main/resources/static/avatars";
        FileUploadUtil.saveFile(uploadDir, filename, multipartFile);

        userRepository.save(dbUser);

        return "redirect:/web/user";
    }

}
