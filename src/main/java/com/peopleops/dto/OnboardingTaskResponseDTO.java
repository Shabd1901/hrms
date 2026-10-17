package com.peopleops.dto;

import com.peopleops.enums.OnboardingTaskStatus;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OnboardingTaskResponseDTO {
    private Long id;
    private Long employeeId;
    private String title;
    private String description;
    private LocalDate dueDate;
    private OnboardingTaskStatus status;
    private LocalDate completedOn;
}
