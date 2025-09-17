package com.bugtracker.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSettingsDTO {
    private String username;
    private String email;
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}
