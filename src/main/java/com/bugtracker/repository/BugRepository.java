package com.bugtracker.repository;

import com.bugtracker.model.Bug;
import com.bugtracker.model.Developer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BugRepository extends JpaRepository<Bug, Long> {
    List<Bug> findByCreatedBy(String createdBy);  
    List<Bug> findByAssignedTo(Developer developer);
    long countByStatus(String status);
}
