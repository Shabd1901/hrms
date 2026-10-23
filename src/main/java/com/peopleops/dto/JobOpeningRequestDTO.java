package com.peopleops.dto;

import jakarta.validation.constraints.Min;
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
public class JobOpeningRequestDTO {

    @NotBlank(message = "Job opening title is required")
    private String title;

    @NotNull(message = "Department id is required")
    private Long departmentId;

    @NotNull(message = "Designation id is required")
    private Long designationId;

    @NotNull(message = "Open positions count is required")
    @Min(value = 1, message = "Open positions must be at least 1")
    private Integer openPositions;

    private LocalDate postedDate;
}
