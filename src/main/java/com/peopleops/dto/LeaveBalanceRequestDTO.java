package com.peopleops.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaveBalanceRequestDTO {

    @NotNull(message = "Employee id is required")
    private Long employeeId;

    @NotNull(message = "Leave type id is required")
    private Long leaveTypeId;

    @NotNull(message = "Balance year is required")
    @Min(value = 2000, message = "Balance year must be valid")
    private Integer balanceYear;

    @NotNull(message = "Total days is required")
    @Min(value = 0, message = "Total days cannot be negative")
    private Integer totalDays;

    @Min(value = 0, message = "Used days cannot be negative")
    private Integer usedDays;
}
