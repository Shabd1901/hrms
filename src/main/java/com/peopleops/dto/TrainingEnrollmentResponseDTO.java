package com.peopleops.dto;

import com.peopleops.enums.EnrollmentStatus;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainingEnrollmentResponseDTO {
    private Long id;
    private Long trainingProgramId;
    private Long employeeId;
    private EnrollmentStatus status;
    private LocalDate completionDate;
}
