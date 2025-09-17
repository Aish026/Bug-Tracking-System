package com.bugtracker.controller;

import com.bugtracker.dto.DeveloperDTO;
import com.bugtracker.service.DeveloperService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/developers")
public class DeveloperController {

    private final DeveloperService developerService;

    public DeveloperController(DeveloperService developerService) {
        this.developerService = developerService;
    }

//    @GetMapping
//    public String listDevelopers(Model model) {
//        List<DeveloperDTO> developers = developerService.getAllDevelopers();
//        model.addAttribute("developers", developers);
//        return "admin/manage-developers";
//    }

    @GetMapping("/add")
    public String addDeveloperForm(Model model) {
        model.addAttribute("developer", new DeveloperDTO());
        return "admin/add-developer"; 
    }

    @PostMapping("/add")
    public String saveDeveloper(@ModelAttribute("developer") DeveloperDTO dto) {
        developerService.createDeveloper(dto);
        return "redirect:/developers";
    }

    @GetMapping("/{id}/edit")
    public String editDeveloper(@PathVariable Long id, Model model) {
        model.addAttribute("developer", developerService.getDeveloperById(id));
        return "admin/edit-developer"; 
    }

    @PostMapping("/update/{id}")
    public String updateDeveloper(@PathVariable Long id, @ModelAttribute DeveloperDTO developer) {
        developerService.updateDeveloper(id, developer);
        return "redirect:/developers";
    }

    @GetMapping("/{id}/delete")
    public String deleteDeveloper(@PathVariable Long id) {
        developerService.deleteDeveloper(id);
        return "redirect:/developers";
    }
}
