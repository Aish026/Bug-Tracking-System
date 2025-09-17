package com.bugtracker.service;

import com.bugtracker.dto.BugDTO;
import com.bugtracker.dto.BugStatsDTO;
import com.bugtracker.model.Bug;
import com.bugtracker.model.Developer;
import com.bugtracker.repository.BugRepository;
import com.bugtracker.repository.DeveloperRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BugService {

    private final BugRepository bugRepository;
    private final DeveloperRepository developerRepository;

    public BugService(BugRepository bugRepository, DeveloperRepository developerRepository) {
        this.bugRepository = bugRepository;
        this.developerRepository = developerRepository;
    }

    public List<BugDTO> getAllBugs() {
        return bugRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BugDTO getBugById(Long id) {
        return bugRepository.findById(id).map(this::convertToDTO).orElse(null);
    }

    public BugDTO createBug(BugDTO bugDTO, String username) {
        bugDTO.setCreatedBy(username); 
        if (bugDTO.getStatus() == null || bugDTO.getStatus().isBlank()) {
            bugDTO.setStatus("OPEN");
        }
        Bug bug = convertToEntity(bugDTO);
        return convertToDTO(bugRepository.save(bug));
    }

    public BugDTO updateBug(Long id, BugDTO bugDTO) {
        return bugRepository.findById(id).map(existingBug -> {
            existingBug.setTitle(bugDTO.getTitle());
            existingBug.setDescription(bugDTO.getDescription());
            existingBug.setStatus(bugDTO.getStatus());
            existingBug.setPriority(bugDTO.getPriority());

            if (bugDTO.getAssignedToId() != null) {
                developerRepository.findById(bugDTO.getAssignedToId())
                        .ifPresent(existingBug::setAssignedTo);
            } else {
                existingBug.setAssignedTo(null);
            }

            return convertToDTO(bugRepository.save(existingBug));
        }).orElse(null);
    }

    public void deleteBug(Long id) {
        bugRepository.deleteById(id);
    }

    public void assignBugToDeveloper(Long bugId, Long developerId) {
        Bug bug = bugRepository.findById(bugId)
                .orElseThrow(() -> new RuntimeException("Bug not found"));

        Developer dev = developerRepository.findById(developerId)
                .orElseThrow(() -> new RuntimeException("Developer not found"));

        bug.setAssignedTo(dev);
        bugRepository.save(bug);
    }

    public List<BugDTO> getTicketsForUser(String username) {
        return bugRepository.findByCreatedBy(username)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Bugs assigned to a developer
    public List<BugDTO> getTicketsForDeveloper(String username) {
        return developerRepository.findByUsername(username)
                .map(dev -> bugRepository.findByAssignedTo(dev))
                .orElse(List.of())
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    //Dashboard
    public BugStatsDTO getBugStatistics() {
        long total = bugRepository.count();
        long open = bugRepository.countByStatus("OPEN");
        long resolved = bugRepository.countByStatus("RESOLVED");

        return BugStatsDTO.builder()
                .total(total)
                .open(open)
                .resolved(resolved)
                .bugs(getAllBugs())
                .build();
    }

    private BugDTO convertToDTO(Bug bug) {
        if (bug == null) return null;
        return BugDTO.builder()
                .id(bug.getId())
                .title(bug.getTitle())
                .description(bug.getDescription())
                .status(bug.getStatus())
                .priority(bug.getPriority())
                .createdBy(bug.getCreatedBy())
                .assignedToId(bug.getAssignedTo() != null ? bug.getAssignedTo().getId() : null)
                .assignedToUsername(bug.getAssignedTo() != null ? bug.getAssignedTo().getUsername() : "Unassigned")
                .build();
    }

    private Bug convertToEntity(BugDTO dto) {
        Bug bug = new Bug();
        bug.setId(dto.getId());
        bug.setTitle(dto.getTitle());
        bug.setDescription(dto.getDescription());
        bug.setStatus(dto.getStatus());
        bug.setPriority(dto.getPriority());
        bug.setCreatedBy(dto.getCreatedBy());

        if (dto.getAssignedToId() != null) {
            developerRepository.findById(dto.getAssignedToId())
                    .ifPresent(bug::setAssignedTo);
        }
        return bug;
    }
}
