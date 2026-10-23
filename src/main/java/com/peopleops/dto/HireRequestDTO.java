package com.peopleops.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Date of joining is required")
    private LocalDate dateOfJoining;

    @NotNull(message = "Monthly salary is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Monthly salary must be greater than 0")
    private BigDecimal monthlySalary;

    private Long managerId;
}
