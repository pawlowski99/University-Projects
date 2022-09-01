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
import pl.pawlowski99.gym.domain.Guide;
import pl.pawlowski99.gym.domain.Guide;
import pl.pawlowski99.gym.service.GuideService;
import pl.pawlowski99.gym.service.GuideService;
import pl.pawlowski99.gym.service.MuscleService;

import javax.validation.Valid;

@Controller
public class GuideWebController {

    private final GuideService guideService;
    private final MuscleService muscleService;

    public GuideWebController(GuideService guideService, MuscleService muscleService) {
        this.guideService = guideService;
        this.muscleService = muscleService;
    }

    //    ADMIN PANEL

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/guide/add")
    public String addAdminGuide(ModelMap model){
        model.addAttribute("guideToSave", new Guide());
        model.addAttribute("muscles", muscleService.getAllMuscles());

        return "admin-guide-save";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/admin/guide")
    public String addAdminGuide(@ModelAttribute("guideToSave") @Valid Guide guide, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("muscles", muscleService.getAllMuscles());
            return "admin-guide-save";
        }
        guideService.saveGuide(guide);

        return "redirect:/admin/guide";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/guide")
    public String getAdminGuides(Model model){
        model.addAttribute("allGuides", guideService.getAllGuides());

        return "admin-guide-all";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/guide/{id}")
    public String getAdminGuide(@PathVariable("id") Long id, ModelMap model){
        if(guideService.getGuideById(id).isPresent()){
            model.addAttribute("guide", guideService.getGuideById(id).get());
            return "admin-guide-details";
        }
        return "error-404";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/guide/edit/{id}")
    public String editAdminGuide(@PathVariable("id") Long id, ModelMap model){
        if(guideService.getGuideById(id).isPresent()) {
            model.addAttribute("muscles", muscleService.getAllMuscles());
            model.addAttribute("guideToSave", guideService.getGuideById(id).get());

            return "admin-guide-save";
        }
        return "error-404";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/guide/delete/{id}")
    public String deleteAdminGuide(@PathVariable("id") Long id, ModelMap model){
        if(guideService.getGuideById(id).isPresent()) {
            guideService.deleteGuideById(id);

            return "redirect:/admin/guide";
        }
        return "error-404";
    }


//    USER WORKOUT


    @GetMapping("/web/guide")
    public String getGuides(Model model){
        model.addAttribute("allGuides", guideService.getAllGuides());

        return "guide-all";
    }


    @GetMapping("/web/guide/{id}")
    public String getGuide(@PathVariable("id") Long id, ModelMap model){
        if(guideService.getGuideById(id).isPresent()){
            model.addAttribute("guide", guideService.getGuideById(id).get());
            return "guide-details";
        }
        return "error-404";
    }


}
