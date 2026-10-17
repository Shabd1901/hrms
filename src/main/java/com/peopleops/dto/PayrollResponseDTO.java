package com.peopleops.dto;

import com.peopleops.enums.PayrollStatus;
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
public class PayrollResponseDTO {
    private Long id;
    private Long employeeId;
    private Integer payMonth;
    private Integer payYear;
    private Integer workingDays;
    private Double lopDays;
    private BigDecimal grossSalary;
    private BigDecimal lopDeduction;
    private BigDecimal pfDeduction;
    private BigDecimal netSalary;
    private PayrollStatus status;
    private LocalDate generatedOn;
}
