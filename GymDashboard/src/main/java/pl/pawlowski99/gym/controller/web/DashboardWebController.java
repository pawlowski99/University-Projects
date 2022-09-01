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
import pl.pawlowski99.gym.service.ExerciseService;
import pl.pawlowski99.gym.service.UserService;
import pl.pawlowski99.gym.service.WorkoutService;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Controller
public class DashboardWebController {

    private final WorkoutService workoutService;
    private final ExerciseService exerciseService;
    private final UserService userService;

    public DashboardWebController(WorkoutService workoutService, ExerciseService exerciseService, UserService userService) {
        this.workoutService = workoutService;
        this.exerciseService = exerciseService;
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/web/stats")
    public String getUserStats(ModelMap model, Principal principal){
        String username = principal == null ? null : principal.getName();
        Optional<User> viewer = userService.getUserByUsername(username);

        if(viewer.isPresent()){
            model.addAttribute("totalWorkouts", workoutService.getAllCompletedUserWorkouts(username, 'D').size());
            model.addAttribute("mostWorkoutsInM",  workoutService.getMostWorkoutsInAMonthForUsername(username));
            model.addAttribute("firstWorkout", workoutService.getFirstWorkoutByUsername(username));
            model.addAttribute("lastWorkout", workoutService.getLastWorkoutByUsername(username));
            model.addAttribute("mostExe", workoutService.getWorkoutWithMostExercisesByUsername(username));
            model.addAttribute("favExe", exerciseService.getFavExeByUser(username));
            model.addAttribute("heaviestEq", exerciseService.getHeaviestByUser(username));
            model.addAttribute("mostMuscle", exerciseService.getMostMuscleByUser(username));
            model.addAttribute("leastMuscle", exerciseService.getLeastMuscleByUser(username));

            return "user-stats";
        }
        return "error-404";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/web/dashboard")
    public String getDashboard(Model model, Principal principal){
        String username = principal == null ? null : principal.getName();
        Optional<User> viewer = userService.getUserByUsername(username);

        if(viewer.isPresent()) {
            model.addAttribute("allCompletedWorkouts", workoutService.getAllCompletedUserWorkouts(username, 'D'));
            model.addAttribute("allPlannedWorkouts", workoutService.getAllPlannedUserWorkouts(username, 'A'));
            model.addAttribute("localDateTime", LocalDate.now());
            model.addAttribute("workoutsM", workoutService.getWorkoutsPerMonthByUsername(username));
            model.addAttribute("weightsM", exerciseService.getWeightsPercentageUsageInMonthByUsername(username));
            model.addAttribute("weightsY", exerciseService.getWeightsPercentageUsageInYearByUsername(username));
            model.addAttribute("plannedForToday", workoutService.getFirstPlannedWorkoutByWorkoutDate(LocalDate.now()));
            model.addAttribute("muscleIntensityM", exerciseService.getMuscleIntMByUsername(username));
            model.addAttribute("muscleIntensityY", exerciseService.getMuscleIntYByUsername(username));

            return "dashboard";
        }
        return "error-404";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/web/dashboard/confirm/{id}")
    public String confirmDashboardWorkout(@PathVariable("id") Long id, ModelMap model, Principal principal){
        String username = principal == null ? null : principal.getName();
        Optional<User> viewer = userService.getUserByUsername(username);
        if(viewer.isPresent() && workoutService.getWorkoutById(id).isPresent()){
            if(Objects.equals(workoutService.getWorkoutById(id).get().getUser().getId(), viewer.get().getId())) {
                Workout workout = workoutService.getWorkoutById(id).get();
                workout.setCompleted();
                workoutService.saveWorkout(workout);

                return "redirect:/web/dashboard";
            }
        }
        return "error-404";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/web/dashboard/delete/{id}")
    public String deleteDashboardWorkout(@PathVariable("id") Long id, ModelMap model, Principal principal){
        String username = principal == null ? null : principal.getName();
        Optional<User> viewer = userService.getUserByUsername(username);
        if(viewer.isPresent() && workoutService.getWorkoutById(id).isPresent()){
            if(Objects.equals(workoutService.getWorkoutById(id).get().getUser().getId(), viewer.get().getId())) {
                workoutService.deleteWorkoutById(id);

                return "redirect:/web/dashboard";
            }
        }
        return "error-404";
    }
}
