package com.peopleops.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HireRequestDTO {
    private LocalDate dateOfJoining;
    private BigDecimal monthlySalary;
    private Long managerId;
}
