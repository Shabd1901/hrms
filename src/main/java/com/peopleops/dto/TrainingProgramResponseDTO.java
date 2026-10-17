package com.peopleops.dto;

import com.peopleops.enums.TrainingStatus;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainingProgramResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String trainerName;
    private LocalDate startDate;
    private LocalDate endDate;
    private TrainingStatus status;
}
