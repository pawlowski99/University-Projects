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
import pl.pawlowski99.gym.domain.Exercise;
import pl.pawlowski99.gym.domain.User;
import pl.pawlowski99.gym.service.ExerciseService;
import pl.pawlowski99.gym.service.GuideService;
import pl.pawlowski99.gym.service.UserService;
import pl.pawlowski99.gym.service.WorkoutService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@Controller
public class ExerciseWebController {

    private final ExerciseService exerciseService;
    private final WorkoutService workoutService;
    private final GuideService guideService;
    private final UserService userService;

    public ExerciseWebController(ExerciseService exerciseService, WorkoutService workoutService, GuideService guideService, UserService userService) {
        this.exerciseService = exerciseService;
        this.workoutService = workoutService;
        this.guideService = guideService;
        this.userService = userService;
    }

//    ADMIN PANEL

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/exercise/add")
    public String addAdminExercise(ModelMap model){
        model.addAttribute("exerciseToSave", new Exercise());

        model.addAttribute("workouts", workoutService.getAllWorkouts());
        model.addAttribute("guides", guideService.getAllGuides());

        return "admin-exercise-save";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/admin/exercise")
    public String addAdminExercise(@ModelAttribute("exerciseToSave") @Valid Exercise exercise, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("workouts", workoutService.getAllWorkouts());
            model.addAttribute("guides", guideService.getAllGuides());
            return "admin-exercise-save";
        }
        exerciseService.saveExercise(exercise);

        return "redirect:/admin/exercise";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/exercise")
    public String getAdminExercises(Model model){

        model.addAttribute("allExercises", exerciseService.getAllExercises());

        return "admin-exercise-all";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/exercise/{id}")
    public String getAdminExercise(@PathVariable("id") Long id, ModelMap model){
        if(exerciseService.getExerciseById(id).isPresent()){
            model.addAttribute("exercise", exerciseService.getExerciseById(id).get());
            return "admin-exercise-details";
        }
        return "error-404";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/exercise/edit/{id}")
    public String editAdminExercise(@PathVariable("id") Long id, ModelMap model){
        Optional<Exercise> exercise = exerciseService.getExerciseById(id);
        if(exercise.isPresent()) {
            model.addAttribute("exerciseToSave", exercise.get());
            model.addAttribute("workouts", workoutService.getAllWorkouts());
            model.addAttribute("guides", guideService.getAllGuides());
            return "admin-exercise-save";
        }
        return "error-404";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/exercise/delete/{id}")
    public String deleteAdminExercise(@PathVariable("id") Long id){
        if(exerciseService.getExerciseById(id).isPresent()) {
            exerciseService.deleteExerciseById(id);
            return "redirect:/admin/exercise";
        }
        return "error-404";
    }


//    USER WORKOUT


    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/web/exercise/addTo/{id}")
    public String addUserExerciseTo(@PathVariable("id") Long id, ModelMap model, Principal principal){
        String username = principal == null ? null : principal.getName();
        Optional<User> viewer = userService.getUserByUsername(username);

        if(viewer.isPresent() && workoutService.getWorkoutById(id).isPresent()) {
            if(workoutService.getWorkoutById(id).get().getUser().getUsername() == username) {
                Exercise exe = new Exercise();
                exe.setWorkout(workoutService.getWorkoutById(id).get());

                model.addAttribute("exerciseToSave", exe);
                model.addAttribute("guides", guideService.getAllGuides());

                return "user-exercise-save";
            }
        }
        return "error-404";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/web/exercise")
    public String addUserExercise(@ModelAttribute("exerciseToSave") @Valid Exercise exercise, BindingResult bindingResult, ModelMap model, Principal principal){
        String username = principal == null ? null : principal.getName();

        if(bindingResult.hasErrors()){
            model.addAttribute("guides", guideService.getAllGuides());
            return "user-exercise-save";
        }
        if(exercise.getWorkout().getUser().getUsername() != username)
        {
            return "error-404";
        }

        exerciseService.saveExercise(exercise);

        return "redirect:/web/workout/"+exercise.getWorkout().getId();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/web/exercise/edit/{id}")
    public String editUserExercise(@PathVariable("id") Long id, ModelMap model, Principal principal){
        String username = principal == null ? null : principal.getName();
        Optional<User> viewer = userService.getUserByUsername(username);

        if(viewer.isPresent() && exerciseService.getExerciseById(id).isPresent()) {
            if(exerciseService.getExerciseById(id).get().getWorkout().getUser().getUsername() == username) {
                model.addAttribute("exerciseToSave", exerciseService.getExerciseById(id).get());
                model.addAttribute("guides", guideService.getAllGuides());

                return "user-exercise-save";
            }
        }
        return "error-404";
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/web/exercise/delete/{id}")
    public String deleteAdminExercise(@PathVariable("id") Long id, Principal principal){
        String username = principal == null ? null : principal.getName();
        Optional<User> viewer = userService.getUserByUsername(username);

        if(viewer.isPresent() && exerciseService.getExerciseById(id).isPresent()) {
            if(exerciseService.getExerciseById(id).get().getWorkout().getUser().getUsername() == username) {
                long wId = exerciseService.getExerciseById(id).get().getWorkout().getId();
                exerciseService.deleteExerciseById(id);

                if (workoutService.getWorkoutById(wId).isPresent()) {
                    return "redirect:/web/workout/" + wId;
                } else {
                    return "redirect:/web/history";
                }
            }
        }

        return "error-404";
    }

}
