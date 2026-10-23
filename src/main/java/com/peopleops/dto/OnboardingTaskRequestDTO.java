package com.peopleops.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OnboardingTaskRequestDTO {

    @NotNull(message = "Employee id is required")
    private Long employeeId;

    @NotBlank(message = "Task title is required")
    private String title;

    private String description;
    private LocalDate dueDate;
}
