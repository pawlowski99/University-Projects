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
import pl.pawlowski99.gym.domain.Workout;
import pl.pawlowski99.gym.service.UserService;
import pl.pawlowski99.gym.service.WorkoutService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@Controller
public class WorkoutWebController {

    private final WorkoutService workoutService;
    private final UserService userService;

    public WorkoutWebController(WorkoutService workoutService, UserService userService) {
        this.workoutService = workoutService;
        this.userService = userService;
    }

//    ADMIN PANEL

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/workout/add")
    public String addAdminWorkout(ModelMap model){
        model.addAttribute("workoutToSave", new Workout());
        model.addAttribute("users", userService.getAllUsers());

        return "admin-workout-save";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/admin/workout")
    public String addAdminWorkout(@ModelAttribute("workoutToSave") @Valid Workout workout, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("users", userService.getAllUsers());
            return "admin-workout-save";
        }
        workoutService.saveWorkout(workout);

        return "redirect:/admin/workout";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/workout")
    public String getAdminWorkouts(Model model){
        model.addAttribute("allWorkouts", workoutService.getAllWorkouts());

        return "admin-workout-all";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/workout/{id}")
    public String getAdminWorkout(@PathVariable("id") Long id, ModelMap model){
        if(workoutService.getWorkoutById(id).isPresent()){
            model.addAttribute("workout", workoutService.getWorkoutById(id).get());
            return "admin-workout-details";
        }
        return "error-404";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/workout/edit/{id}")
    public String editAdminWorkout(@PathVariable("id") Long id, ModelMap model){
        if(workoutService.getWorkoutById(id).isPresent()) {
            model.addAttribute("workoutToSave", workoutService.getWorkoutById(id).get());
            model.addAttribute("users", userService.getAllUsers());
            return "admin-workout-save";
        }
        return "error-404";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/workout/delete/{id}")
    public String deleteAdminWorkout(@PathVariable("id") Long id, ModelMap model){
        if(workoutService.getWorkoutById(id).isPresent()) {
            workoutService.deleteWorkoutById(id);

            return "redirect:/admin/workout";
        }
        return "error-404";
    }

//    USER

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/web/history")
    public String getAllHistory(ModelMap model, Principal principal){
        String username = principal == null ? null : principal.getName();
        Optional<User> viewer = userService.getUserByUsername(username);

        if(viewer.isPresent()){
            model.addAttribute("workouts", workoutService.getAllUserWorkouts(username, 'D'));

            return "user-history-all";
        }
        return "error-404";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/web/history/completed")
    public String getCompletedHistory(ModelMap model, Principal principal){
        String username = principal == null ? null : principal.getName();
        Optional<User> viewer = userService.getUserByUsername(username);

        if(viewer.isPresent()){
            model.addAttribute("workouts", workoutService.getAllCompletedUserWorkouts(username, 'D'));

            return "user-history-completed";
        }
        return "error-404";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/web/history/planned")
    public String getPlannedHistory(ModelMap model, Principal principal){
        String username = principal == null ? null : principal.getName();
        Optional<User> viewer = userService.getUserByUsername(username);

        if(viewer.isPresent()){
            model.addAttribute("workouts", workoutService.getAllPlannedUserWorkouts(username, 'A'));

            return "user-history-planned";
        }
        return "error-404";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/web/workout/{id}")
    public String getUserWorkout(@PathVariable("id") Long id, ModelMap model, Principal principal){
        String username = principal == null ? null : principal.getName();
        Optional<User> viewer = userService.getUserByUsername(username);

        if(workoutService.getWorkoutById(id).isPresent() && viewer.isPresent()){
            if(workoutService.getWorkoutById(id).get().getUser().getUsername() == username){
                model.addAttribute("workout", workoutService.getWorkoutById(id).get());
                return "user-workout-details";
            }
        }
        return "error-404";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/web/workout/delete/{id}")
    public String deleteUserWorkout(@PathVariable("id") Long id, ModelMap model, Principal principal){
        String username = principal == null ? null : principal.getName();
        Optional<User> viewer = userService.getUserByUsername(username);

        if(workoutService.getWorkoutById(id).isPresent() && viewer.isPresent()){
            if(workoutService.getWorkoutById(id).get().getUser().getUsername() == username){
                workoutService.deleteWorkoutById(id);
                return "redirect:/web/history";
            }
        }

        return "error-404";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/web/workout/add")
    public String addUserWorkout(ModelMap model, Principal principal){
        String username = principal == null ? null : principal.getName();
        Optional<User> viewer = userService.getUserByUsername(username);

        if(viewer.isPresent()){
            Workout newW = new Workout();
            newW.setUser(userService.getUserByUsername(username).get());
            newW.setWorkoutStatus(0);
            model.addAttribute("workoutToSave", newW);

            return "user-workout-save";
        }
        return "error-404";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/web/workout/add")
    public String addUserWorkout(@ModelAttribute("workoutToSave") @Valid Workout workout, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "user-workout-save";
        }

        workoutService.saveWorkout(workout);

        return "redirect:/web/workout/"+workout.getId();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/web/workout/edit/{id}")
    public String editUserWorkout(@PathVariable("id") Long id, ModelMap model, Principal principal){
        String username = principal == null ? null : principal.getName();
        Optional<User> viewer = userService.getUserByUsername(username);

        if(viewer.isPresent() && workoutService.getWorkoutById(id).isPresent()){
            if(workoutService.getWorkoutById(id).get().getUser().getId() == viewer.get().getId()) {
                model.addAttribute("workoutToSave", workoutService.getWorkoutById(id).get());
                return "user-workout-save";
            }
        }
        return "error-404";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/web/workout/edit")
    public String editUserWorkout(@ModelAttribute("workoutToSave") @Valid Workout workout, BindingResult bindingResult, ModelMap model, Principal principal){
        String username = principal == null ? null : principal.getName();

        if(bindingResult.hasErrors()){
            return "user-workout-save";
        }

//        if(workout.getUser().getId() != userService.getUserByUsername(username).get().getId())
//        {
//            return "error-404";
//        }

        workoutService.saveWorkout(workout);

        return "redirect:/web/workout/"+workout.getId();
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/web/workout/plan")
    public String planUserWorkout(ModelMap model, Principal principal){
        String username = principal == null ? null : principal.getName();
        Optional<User> viewer = userService.getUserByUsername(username);

        if(viewer.isPresent()){
            Workout newW = new Workout();
            newW.setUser(userService.getUserByUsername(username).get());
            newW.setWorkoutStatus(1);
            model.addAttribute("workoutToSave", newW);

            return "user-workout-save";
        }
        return "error-404";
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/web/workout/setPlanned/{id}")
    public String setUserWorkoutPlanned(@PathVariable("id") Long id, ModelMap model, Principal principal){
        String username = principal == null ? null : principal.getName();
        Optional<User> viewer = userService.getUserByUsername(username);

        if(viewer.isPresent() && workoutService.getWorkoutById(id).isPresent()){
            if(workoutService.getWorkoutById(id).get().getUser().getId() == viewer.get().getId()) {
                Workout workout = workoutService.getWorkoutById(id).get();
                workout.setPlanned();
                workoutService.saveWorkout(workout);
                return "redirect:/web/workout/" + id;
            }
        }
        return "error-404";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/web/workout/setCompleted/{id}")
    public String setUserWorkoutCompleted(@PathVariable("id") Long id, ModelMap model, Principal principal){
        String username = principal == null ? null : principal.getName();
        Optional<User> viewer = userService.getUserByUsername(username);

        if(viewer.isPresent() && workoutService.getWorkoutById(id).isPresent()){
            if(workoutService.getWorkoutById(id).get().getUser().getId() == viewer.get().getId()) {
                Workout workout = workoutService.getWorkoutById(id).get();
                workout.setCompleted();
                workoutService.saveWorkout(workout);
                return "redirect:/web/workout/" + id;
            }
        }
        return "error-404";
    }

}
