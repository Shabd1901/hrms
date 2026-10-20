package com.peopleops.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OffboardingRequestDTO {
    private Long employeeId;
    private LocalDate resignationDate;
    private LocalDate lastWorkingDate;
    private String reason;
    private String exitInterviewNotes;
}
