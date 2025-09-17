package com.bugtracker.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeveloperDTO {
    private Long id; 
    private String username;
    private String email;
}
