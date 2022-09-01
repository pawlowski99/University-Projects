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
import pl.pawlowski99.gym.domain.Muscle;
import pl.pawlowski99.gym.domain.Muscle;
import pl.pawlowski99.gym.service.MuscleService;
import pl.pawlowski99.gym.service.MuscleService;

import javax.validation.Valid;

@Controller
public class MuscleWebController {

    private final MuscleService muscleService;

    public MuscleWebController(@Autowired MuscleService muscleService) {
        this.muscleService = muscleService;
    }

//    ADMIN PANEL

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/muscle/add")
    public String addAdminMuscle(ModelMap model){
        model.addAttribute("muscleToSave", new Muscle());

        return "admin-muscle-save";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/admin/muscle")
    public String addAdminMuscle(@ModelAttribute("muscleToSave") @Valid Muscle muscle, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "admin-muscle-save";
        }
        muscleService.saveMuscle(muscle);

        return "redirect:/admin/muscle";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/muscle")
    public String getAdminMuscles(Model model){
        model.addAttribute("allMuscles", muscleService.getAllMuscles());

        return "admin-muscle-all";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/muscle/{id}")
    public String getAdminMuscle(@PathVariable("id") Long id, ModelMap model){
        if(muscleService.getMuscleById(id).isPresent()){
            model.addAttribute("muscle", muscleService.getMuscleById(id).get());
            return "admin-muscle-details";
        }
        return "error-404";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/muscle/edit/{id}")
    public String editAdminMuscle(@PathVariable("id") Long id, ModelMap model){
        if(muscleService.getMuscleById(id).isPresent()) {
            model.addAttribute("muscleToSave", muscleService.getMuscleById(id).get());

            return "admin-muscle-save";
        }
        return "error-404";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/muscle/delete/{id}")
    public String deleteAdminMuscle(@PathVariable("id") Long id, ModelMap model){
        if(muscleService.getMuscleById(id).isPresent()) {
            muscleService.deleteMuscleById(id);

            return "redirect:/admin/muscle";
        }
        return "error-404";
    }

}
