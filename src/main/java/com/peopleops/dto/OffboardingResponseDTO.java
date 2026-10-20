package com.peopleops.dto;

import com.peopleops.enums.OffboardingStatus;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OffboardingResponseDTO {
    private Long id;
    private Long employeeId;
    private LocalDate resignationDate;
    private LocalDate lastWorkingDate;
    private String reason;
    private String exitInterviewNotes;
    private OffboardingStatus status;
}
