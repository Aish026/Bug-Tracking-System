package com.bugtracker.controller;

import com.bugtracker.dto.BugDTO;
import com.bugtracker.service.BugService;
import com.bugtracker.repository.DeveloperRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/bugs")
public class BugController {

    private final BugService bugService;
    private final DeveloperRepository developerRepository;

    public BugController(BugService bugService, DeveloperRepository developerRepository) {
        this.bugService = bugService;
        this.developerRepository = developerRepository;
    }

    // everyone can view all bugs
    @GetMapping
    public String listBugs(Model model) {
        model.addAttribute("bugs", bugService.getAllBugs());
        return "bugs";
    }

    // Reporting a bug
    @GetMapping("/report")
    public String reportBugForm(Model model) {
        model.addAttribute("bug", new BugDTO());
        return "report-bug"; // report-bug.html
    }
    // View particular bug details
    @GetMapping("/view/{id}")
      public String getBugById(@PathVariable Long id, Model model) {
    	BugDTO bug = bugService.getBugById(id);
    	model.addAttribute("bug", bug);
        return "bug-details";
    }

    // Developer and Admin can edit bug
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, Authentication auth) {
        BugDTO bug = bugService.getBugById(id);
        model.addAttribute("bug", bug);

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);

        if (isAdmin) {
            model.addAttribute("developers", developerRepository.findAll());
        }

        return "edit-bug"; 
    }

    @PostMapping("/update/{id}")
    public String updateBug(@PathVariable Long id,
                            @ModelAttribute("bug") BugDTO bugDTO,
                            Authentication auth) {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            bugService.updateBug(id, bugDTO); // full update
        } else {
            // developers only update status/priority
            BugDTO existing = bugService.getBugById(id);
            existing.setStatus(bugDTO.getStatus());
            existing.setPriority(bugDTO.getPriority());
            bugService.updateBug(id, existing);
        }

        return "redirect:/bugs";
    }

    // Admin can assign bug to developer
    @PostMapping("/assign/{bugId}/{devId}")
    public String assignBug(@PathVariable Long bugId, @PathVariable Long devId) {
        bugService.assignBugToDeveloper(bugId, devId);
        return "redirect:/bugs";
    }

    //Deleting a Bug 
    @GetMapping("/delete/{id}")
    public String deleteBug(@PathVariable Long id) {
        bugService.deleteBug(id);
        return "redirect:/bugs";
    }

    //Developer can view bugs assigned to them
    @GetMapping("/my-assigned")
    public String myAssignedBugs(Authentication auth, Model model) {
        String username = auth.getName();
        model.addAttribute("bugs", bugService.getTicketsForDeveloper(username));
        return "developer/my-bugs";
    }

    // Bugs reported by particular user
    @GetMapping("/my-reported")
    public String myReportedBugs(Authentication auth, Model model) {
        String username = auth.getName();
        model.addAttribute("username", username);
        model.addAttribute("bugs", bugService.getTicketsForUser(username));
        return "my-reported-bugs"; 
    }
    @PostMapping("/submit")
    public String submitBug(@ModelAttribute("bug") BugDTO bugDTO, Authentication auth) {
        bugService.createBug(bugDTO, auth.getName()); 
        return "redirect:/bugs";
    }
}
