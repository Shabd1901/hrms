package com.peopleops.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainingEnrollmentRequestDTO {

    @NotNull(message = "Training program id is required")
    private Long trainingProgramId;

    @NotNull(message = "Employee id is required")
    private Long employeeId;
}
