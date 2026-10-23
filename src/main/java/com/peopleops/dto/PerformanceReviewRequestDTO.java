package com.peopleops.dto;

import jakarta.validation.constraints.Max;
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
public class PerformanceReviewRequestDTO {

    @NotNull(message = "Employee id is required")
    private Long employeeId;

    @NotNull(message = "Reviewer id is required")
    private Long reviewerId;

    @NotBlank(message = "Review period is required")
    private String reviewPeriod;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be between 1 and 5")
    @Max(value = 5, message = "Rating must be between 1 and 5")
    private Integer rating;

    private String comments;

    private LocalDate reviewDate;
}
