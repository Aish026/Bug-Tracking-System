package com.bugtracker.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BugDTO {
    private Long id;
    private String title;
    private String description;
    private String status = "OPEN";
    private String priority;
    private String createdBy;     
    private Long assignedToId;    
    private String assignedToUsername; 
}
