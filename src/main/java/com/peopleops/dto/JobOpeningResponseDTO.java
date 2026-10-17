package com.peopleops.dto;

import com.peopleops.enums.JobOpeningStatus;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobOpeningResponseDTO {
    private Long id;
    private String title;
    private Long departmentId;
    private Long designationId;
    private Integer openPositions;
    private LocalDate postedDate;
    private JobOpeningStatus status;
}
