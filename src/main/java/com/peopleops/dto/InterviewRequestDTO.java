package com.peopleops.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewRequestDTO {
    private Long candidateId;
    private Long interviewerId;
    private LocalDateTime scheduledAt;
    private Integer roundNumber;
    private String feedback;
}
