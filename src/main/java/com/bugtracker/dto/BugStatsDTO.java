package com.bugtracker.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BugStatsDTO {
    private long total;
    private long open;
    private long resolved;
    private List<BugDTO> bugs;
}
