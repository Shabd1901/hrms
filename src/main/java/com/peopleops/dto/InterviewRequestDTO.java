package com.peopleops.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Candidate id is required")
    private Long candidateId;

    @NotNull(message = "Interviewer id is required")
    private Long interviewerId;

    @NotNull(message = "Scheduled time is required")
    private LocalDateTime scheduledAt;

    @NotNull(message = "Round number is required")
    @Min(value = 1, message = "Round number must be at least 1")
    private Integer roundNumber;

    private String feedback;
}
