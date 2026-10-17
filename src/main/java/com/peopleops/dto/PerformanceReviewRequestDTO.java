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
public class PerformanceReviewRequestDTO {
    private Long employeeId;
    private Long reviewerId;
    private String reviewPeriod;
    private Integer rating;
    private String comments;
    private LocalDate reviewDate;
}
