package com.peopleops.dto;

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
public class OffboardingRequestDTO {

    @NotNull(message = "Employee id is required")
    private Long employeeId;

    @NotNull(message = "Resignation date is required")
    private LocalDate resignationDate;

    @NotNull(message = "Last working date is required")
    private LocalDate lastWorkingDate;

    private String reason;

    private String exitInterviewNotes;
}
