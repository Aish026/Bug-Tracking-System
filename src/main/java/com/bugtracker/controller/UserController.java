package com.bugtracker.controller;

import com.bugtracker.dto.UserSettingsDTO;
import com.bugtracker.model.User;   // ✅ import User entity
import com.bugtracker.service.UserService;
import com.bugtracker.repository.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }
    
    // Can change email or password username can not be changed
    @GetMapping("/settings")
    public String getUserSettings(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                                  .orElseThrow(() -> new RuntimeException("User not found"));
        UserSettingsDTO dto = new UserSettingsDTO();
        dto.setEmail(user.getEmail());
        dto.setNewPassword(""); 
        model.addAttribute("settings", dto); 
        return "user-settings"; 
    }

    @PostMapping("/settings/update")
    public String updateUserSettings(@AuthenticationPrincipal UserDetails userDetails,
                                     @ModelAttribute("settings") UserSettingsDTO dto) {
        userService.updateUserSettings(userDetails.getUsername(), dto);
        return "redirect:/users/settings?success";
    }
}
