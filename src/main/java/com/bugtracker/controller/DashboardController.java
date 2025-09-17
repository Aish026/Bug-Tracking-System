package com.bugtracker.controller;

import com.bugtracker.service.BugService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final BugService bugService;

    public DashboardController(BugService bugService) {
        this.bugService = bugService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model, Authentication auth) {
        var stats = bugService.getBugStatistics();

        model.addAttribute("username", auth.getName());
        model.addAttribute("bugs", stats.getBugs());
        model.addAttribute("totalBugs", stats.getTotal());
        model.addAttribute("openTickets", stats.getOpen());
        model.addAttribute("resolvedBugs", stats.getResolved());

        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "admin/dashboard";
        } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DEVELOPER"))) {
            return "developer/dashboard";
        } else {
            return "user/dashboard";
        }
    }
}
