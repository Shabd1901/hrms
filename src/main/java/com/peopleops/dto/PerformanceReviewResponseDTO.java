package com.peopleops.dto;

import com.peopleops.enums.ReviewStatus;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceReviewResponseDTO {
    private Long id;
    private Long employeeId;
    private Long reviewerId;
    private String reviewPeriod;
    private Integer rating;
    private String comments;
    private ReviewStatus status;
    private LocalDate reviewDate;
}
