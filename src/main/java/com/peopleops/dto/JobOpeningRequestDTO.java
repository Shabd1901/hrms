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
public class JobOpeningRequestDTO {
    private String title;
    private Long departmentId;
    private Long designationId;
    private Integer openPositions;
    private LocalDate postedDate;
}
