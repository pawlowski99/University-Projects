package pl.pawlowski99.gym.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.pawlowski99.gym.domain.User;
import pl.pawlowski99.gym.domain.User;
import pl.pawlowski99.gym.service.UserService;
import pl.pawlowski99.gym.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@Controller
public class UserWebController {

    private final UserService userService;

    public UserWebController(@Autowired UserService userService) {
        this.userService = userService;
    }

//    ADMIN PANEL

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/user/add")
    public String addAdminUser(ModelMap model){
        model.addAttribute("userToSave", new User());

        return "admin-user-register";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/admin/user/add")
    public String addAdminUser(@ModelAttribute("userToSave") @Valid User user, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "admin-user-register";
        }

        if(userService.getUserByUsername(user.getUsername()).isPresent()){
            model.addAttribute("usernameTaken", 1);
            return "admin-user-register";
        }

        userService.registerUserAdmin(user);

        return "redirect:/admin/user";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/user")
    public String getAdminUsers(Model model){
        model.addAttribute("allUsers", userService.getAllUsers());

        return "admin-user-all";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/user/{id}")
    public String getAdminUser(@PathVariable("id") Long id, ModelMap model){
        if(userService.getUserById(id).isPresent()){
            model.addAttribute("user", userService.getUserById(id).get());
            return "admin-user-details";
        }
        return "error-404";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/user/edit/{id}")
    public String editAdminUser(@PathVariable("id") Long id, ModelMap model){
        if(userService.getUserById(id).isPresent()) {
            model.addAttribute("userToSave", userService.getUserById(id).get());

            return "admin-user-edit-details";
        }
        return "error-404";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/admin/user/edit")
    public String editAdminUser(@ModelAttribute("userToSave") @Valid User user, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "admin-user-edit-details";
        }

        if(userService.getUserByUsername(user.getUsername()).isPresent()){
            int a = Math.toIntExact(userService.getUserByUsername(user.getUsername()).get().getId());
            int b = Math.toIntExact(user.getId());
            if(a != b) {
                model.addAttribute("usernameTaken", 1);
                return "admin-user-edit-details";
            }
        }

        userService.registerUserAdmin(user);

        return "redirect:/admin/user";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/user/password/{id}")
    public String editPasswordAdminUser(@PathVariable("id") Long id, ModelMap model){
        if(userService.getUserById(id).isPresent()) {
            model.addAttribute("userToSave", userService.getUserById(id).get());
            model.addAttribute("uname", userService.getUserById(id).get().getUsername());

            return "admin-user-edit-password";
        }
        return "error-404";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/admin/user/password")
    public String editPasswordAdminUser(@ModelAttribute("userToSave") @Valid User user, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("uname", user.getUsername());
            return "admin-user-edit-password";
        }

        userService.registerUserAdmin(user);

        return "redirect:/admin/user";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/user/delete/{id}")
    public String deleteAdminUser(@PathVariable("id") Long id, ModelMap model){
        if(userService.getUserById(id).isPresent()) {
            userService.deleteUserById(id);

            return "redirect:/admin/user";
        }
        return "error-404";
    }


//    USER

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/web/profile")
    public String getProfile(ModelMap model, Principal principal){
        String username = principal == null ? null : principal.getName();
        Optional<User> viewer = userService.getUserByUsername(username);

        if(viewer.isPresent()){
            model.addAttribute("user", viewer.get());

            return "user-profile";
        }

        return "error-404";
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/web/user/edit/{id}")
    public String editUser(@PathVariable("id") Long id, ModelMap model, Principal principal){
        String username = principal == null ? null : principal.getName();
        Optional<User> viewer = userService.getUserByUsername(username);

        if(userService.getUserById(id).isPresent()) {
            if(userService.getUserById(id).get().getUsername() == username){
                model.addAttribute("userToSave", userService.getUserById(id).get());

                return "user-edit-details";
            }
        }
        return "error-404";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/web/user/edit")
    public String editUser(@ModelAttribute("userToSave") @Valid User user, BindingResult bindingResult, ModelMap model, Principal principal){
        String username = principal == null ? null : principal.getName();

        if(bindingResult.hasErrors()){
            return "user-edit-details";
        }

        if(user.getId() != userService.getUserByUsername(username).get().getId())
        {
            return "error-404";
        }

        if(userService.getUserByUsername(user.getUsername()).isPresent()){
            int a = Math.toIntExact(userService.getUserByUsername(user.getUsername()).get().getId());
            int b = Math.toIntExact(user.getId());
            if(a != b) {
                model.addAttribute("usernameTaken", 1);
                return "user-edit-details";
            }
        }

        userService.editUser(user);

        return "redirect:/web/profile";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/web/user/password/{id}")
    public String editPasswordUser(@PathVariable("id") Long id, ModelMap model, Principal principal){
        String username = principal == null ? null : principal.getName();

        if(userService.getUserById(id).isPresent()) {
            if(userService.getUserById(id).get().getUsername() == username) {
                model.addAttribute("userToSave", userService.getUserById(id).get());
                model.addAttribute("uname", userService.getUserById(id).get().getUsername());

                return "user-edit-password";
            }
        }
        return "error-404";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/web/user/password")
    public String editPasswordUser(@ModelAttribute("userToSave") @Valid User user, BindingResult bindingResult, ModelMap model, Principal principal){
        String username = principal == null ? null : principal.getName();

        if(bindingResult.hasErrors()){
            model.addAttribute("uname", user.getUsername());
            return "user-edit-password";
        }

//        if(user.getId() != userService.getUserByUsername(username).get().getId())
//        {
//            return "error-404";
//        }

        userService.editUser(user);

        return "redirect:/web/profile";
    }


}
