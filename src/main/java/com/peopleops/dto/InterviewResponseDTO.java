package com.peopleops.dto;

import com.peopleops.enums.InterviewResult;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewResponseDTO {
    private Long id;
    private Long candidateId;
    private Long interviewerId;
    private LocalDateTime scheduledAt;
    private Integer roundNumber;
    private InterviewResult result;
    private String feedback;
}
