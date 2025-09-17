package com.bugtracker.controller;

import com.bugtracker.model.Developer;
import com.bugtracker.model.User;
import com.bugtracker.repository.DeveloperRepository;
import com.bugtracker.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final DeveloperRepository developerRepository;

    public AdminController(UserRepository userRepository, DeveloperRepository developerRepository) {
        this.userRepository = userRepository;
        this.developerRepository = developerRepository;
    }

    // Admin Dashboard
    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "admin/dashboard";
    }

    // Manage All Users
    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/manage-users";
    }

    // Promote user to developer
    @GetMapping("/users/promote/{id}")
    public String promoteToDeveloper(@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow();
        user.setRole("ROLE_DEVELOPER");
        userRepository.save(user);

        // ensure developer entry exists
        developerRepository.findByUsername(user.getUsername())
                .orElseGet(() -> {
                    Developer dev = new Developer();
                    dev.setUsername(user.getUsername());
                    dev.setEmail(user.getEmail());
                    return developerRepository.save(dev);
                });

        return "redirect:/admin/users";
    }

    // Demote developer back to user
    @GetMapping("/users/demote/{id}")
    public String removeDeveloper(@PathVariable Long id) {
        userRepository.findById(id).ifPresent(user -> {
            user.setRole("ROLE_USER");
            userRepository.save(user);

            // also remove from developer table if exists
            developerRepository.findByUsername(user.getUsername())
                    .ifPresent(dev -> developerRepository.delete(dev));
        });
        return "redirect:/admin/users";
    }

    // Delete user (and developer if applicable)
    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.findById(id).ifPresent(user -> {
            developerRepository.findByUsername(user.getUsername())
                    .ifPresent(dev -> developerRepository.delete(dev));
            userRepository.delete(user);
        });
        return "redirect:/admin/users";
    }

    // Manage Developers (pull from Developer table)
    @GetMapping("/developers")
    public String listDevelopers(Model model) {
        List<Developer> developers = developerRepository.findAll();
        model.addAttribute("developers", developers);
        return "admin/manage-developers";
    }
}
